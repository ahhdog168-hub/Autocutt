@RestController
public class VideoController {
    @PostMapping("/process")
    public ResponseEntity<Map<String,Object>> handleUpload(@RequestParam("video") MultipartFile file) {
        Map<String,Object> resp = new HashMap<>();
        try {
            File in = File.createTempFile("in", ".mp4");
            file.transferTo(in);
            // extract audio
            File audio = new File(in.getParent(), "audio.wav");
            new ProcessBuilder("ffmpeg", "-i", in.getAbsolutePath(),
                "-vn", "-acodec", "pcm_s16le", "-ar", "44100", "-ac", "2", audio.getAbsolutePath())
                .inheritIO().start().waitFor();

            List<int[]> cuts = ACRCloudScanner.scan(audio);
            File finalVideo = VideoProcessor.cutAndEnhance(in, cuts);

            // send to Telegram
            TelegramSender.sendVideo("7301967229:AAFnI8isK2YwBYs4MfMvuUFz5tJkYpX2scM", "5252397277", finalVideo);

            // upload to S3
            S3Uploader.uploadFile(finalVideo, "your-bucket-name", finalVideo.getName());

            resp.put("success", true);
            resp.put("url", "/videos/" + finalVideo.getName());
            return ResponseEntity.ok(resp);

        } catch (Exception ex) {
            resp.put("success", false);
            resp.put("message", ex.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }
}
