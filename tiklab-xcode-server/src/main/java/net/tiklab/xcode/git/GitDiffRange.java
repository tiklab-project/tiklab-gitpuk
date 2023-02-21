package net.tiklab.xcode.git;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitDiffRange {
    public static void main(String[] args) {
        String line = "@@ -17,10 +1,10 @@";
        Pattern pattern = Pattern.compile("@@ -(\\d+)(?:,(\\d+))? \\+(\\d+)(?:,(\\d+))? @@.*");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            int oldStart = Integer.parseInt(matcher.group(1));
            int oldLines = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 1;
            int newStart = Integer.parseInt(matcher.group(3));
            int newLines = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 1;
            System.out.println("oldStart：" + oldStart);
            System.out.println("oldLines：" + oldLines);
            System.out.println("newStart：" + newStart);
            System.out.println("newLines：" + newLines);
        }
    }
}

