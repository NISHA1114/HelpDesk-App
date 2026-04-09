import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class App {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // HOME PAGE
        server.createContext("/", exchange -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

                String response =
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "body {margin:0;font-family:Arial;background:#0f172a;color:white;" +
                        "display:flex;justify-content:center;align-items:center;height:100vh;}" +

                        ".container {width:420px;background:#1e293b;padding:25px;border-radius:12px;" +
                        "box-shadow:0 4px 20px rgba(0,0,0,0.5);text-align:center;}" +

                        "h2 {font-size:28px;margin-bottom:15px;}" +

                        "input, textarea, select {width:95%;padding:12px;margin:8px;font-size:16px;" +
                        "border:none;border-radius:6px;}" +

                        "button {padding:12px 20px;font-size:16px;background:#22c55e;color:white;" +
                        "border:none;border-radius:6px;cursor:pointer;margin-top:10px;}" +

                        "</style>" +
                        "</head>" +

                        "<body>" +
                        "<div class='container'>" +

                        "<h2>Create Help Desk Ticket</h2>" +

                        "<form method='POST' action='/create'>" +
                        "<input name='name' placeholder='Your Name' required/>" +
                        "<input name='email' placeholder='Your Email' required/>" +
                        "<input name='title' placeholder='Issue Title' required/>" +
                        "<textarea name='desc' placeholder='Describe issue'></textarea>" +

                        "<select name='priority'>" +
                        "<option>Low</option>" +
                        "<option>Medium</option>" +
                        "<option>High</option>" +
                        "</select>" +

                        "<button type='submit'>Create Ticket</button>" +
                        "</form>" +

                        "</div>" +
                        "</body>" +
                        "</html>";

                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        // CREATE TICKET
        server.createContext("/create", exchange -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));

                String formData = br.readLine();

                String name = "", email = "", title = "", desc = "", priority = "";

                if (formData != null) {
                    String[] pairs = formData.split("&");

                    for (String pair : pairs) {
                        String[] keyValue = pair.split("=");
                        String key = keyValue[0];
                        String value = keyValue.length > 1 ?
                                URLDecoder.decode(keyValue[1], "UTF-8") : "";

                        switch (key) {
                            case "name": name = value; break;
                            case "email": email = value; break;
                            case "title": title = value; break;
                            case "desc": desc = value; break;
                            case "priority": priority = value; break;
                        }
                    }
                }

                String response =
                        "<html>" +
                        "<body style='margin:0;font-family:Arial;background:#0f172a;color:white;" +
                        "display:flex;justify-content:center;align-items:center;height:100vh;'>" +

                        "<div style='background:#1e293b;padding:30px;border-radius:12px;text-align:center;'>" +

                        "<h2>✅ Ticket Created Successfully!</h2>" +

                        "<p><b>Name:</b> " + name + "</p>" +
                        "<p><b>Email:</b> " + email + "</p>" +
                        "<p><b>Issue:</b> " + title + "</p>" +
                        "<p><b>Description:</b> " + desc + "</p>" +
                        "<p><b>Priority:</b> " + priority + "</p>" +

                        "<br><a href='/' style='color:#22c55e;font-size:18px;'>Create Another Ticket</a>" +

                        "</div>" +
                        "</body></html>";

                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.start();
        System.out.println("V1 running on port 8081...");
    }
}