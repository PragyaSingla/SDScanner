# SDScanner

External Storage Scanner


An Android app that scans all external storage and shows the largest files.


- App allows the user to start and stop scanning.
- Displaying progress of ongoing scan.
- App allow user to share obtained statistics of scan.
- Showed Notification while it scans the external storage.
- Handled screen orientation changes.

Below code for scanning:


    private void scanFiles(String path) {
        File[] files = new File(path).listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (fileScannerTask.isCancelled()) {
                    break;
                }

                try {
                    Thread.sleep(1);
                    updateProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (file.isDirectory()) {
                    scanFiles(file.getPath());
                }

                String fileName = file.getName();
                this.fileInfoList.add(new FileInfo(fileName, file.length()));
                String fileExt = FileUtils.getFileExt(fileName);
                if (this.fileExts.containsKey(fileExt)) {
                    this.fileExts.put(fileExt, this.fileExts.get(fileExt) + 1);
                } else {
                    this.fileExts.put(fileExt, 1);
                }
            }
        }
    }
