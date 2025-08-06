public class TelegramSender {
    public static void sendVideo(String botToken, String chatId, File video) throws IOException {
        String url = "https://api.telegram.org/bot" + botToken + "/sendVideo";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        String boundary = "===" + System.currentTimeMillis() + "===";
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        OutputStream out = conn.getOutputStream();
        PrintWriter w = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true);
        w.append("--").append(boundary).append("\r\n")
         .append("Content-Disposition: form-data; name=\"chat_id\"\r\n\r\n")
         .append(chatId).append("\r\n");
        w.append("--").append(boundary).append("\r\n")
         .append("Content-Disposition: form-data; name=\"video\"; filename=\"")
         .append(video.getName()).append("\"\r\n")
         .append("Content-Type: video/mp4\r\n\r\n").flush();
        Files.copy(video.toPath(), out);
        out.flush();
        w.append("\r\n--").append(boundary).append("--\r\n").flush();
        w.close();
        int code = conn.getResponseCode();
        System.out.println("Telegram response: " + code);
    }
}
