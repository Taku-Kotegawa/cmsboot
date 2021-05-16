package jp.co.stnet.cms.common.message;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.regex.Pattern;

public class MessageKeysGen {
    public static void main(String[] args) throws IOException {
        // message properties file

        String[] messageProperties = {"src/main/resources/messages.properties", "src/main/resources/ValidationMessages.properties"};

        Class<?> targetClazz = MessageKeys.class;

        File output = new File("src/main/java/"
                + targetClazz.getName().replaceAll(Pattern.quote("."), "/")
                + ".java");
        System.out.println("write " + output.getAbsolutePath());
        PrintWriter pw = new PrintWriter(FileUtils.openOutputStream(output));

        try {
            pw.println("package " + targetClazz.getPackage().getName() + ";");
            pw.println("/**");
            pw.println(" * Message Id");
            pw.println(" */");
            pw.println("public class " + targetClazz.getSimpleName() + " {");

            String line;

            for(int i = 0; i < messageProperties.length ;i++) {

                InputStream inputStream = new FileInputStream(messageProperties[i]);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = br.readLine()) != null) {

                    // コメント行を読み飛ばす
                    if (line.startsWith("#")) {
                        continue;
                    }

                    String[] vals = line.split("=", 2);
                    if (vals.length > 1) {
                        String key = vals[0].trim();
                        String value = vals[1].trim();
                        pw.println("    /** " + key + "=" + value + " */");
                        pw.println("    public static final String "
                                + key.toUpperCase().replaceAll(Pattern.quote("."),
                                "_").replaceAll(Pattern.quote("-"), "_")
                                + " = \"" + key + "\";");
                    }
                }
                br.close();

            }

            pw.println("}");
            pw.flush();
        } finally {
            pw.close();
        }
    }
}