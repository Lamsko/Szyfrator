import java.io.*;
import java.nio.charset.Charset;

public class Encrypt {
	public static boolean encrypt(File file, String key, UI ui) {
		int blockLength = key.length();
		int currentChar = 0;
		int currentBlock = 0;
		File resultFile = new File(
				file.getAbsolutePath().replace(".txt", ".enc"));

		if (resultFile.exists()) {
			for (int i = 0; i < 100; i++) {
				resultFile = new File(
						file.getAbsolutePath().replace(".txt", "(" + i + ").enc"));
				if (!resultFile.exists())
					break;
			}
		}

		Charset charset = Charset.forName("ascii");

		try {
			InputStream inputStream = new FileInputStream(file);
			OutputStream outputStream = new FileOutputStream(resultFile);
			Writer writer = new OutputStreamWriter(outputStream, charset);

			System.out.println(inputStream.available());

			int currentCharCode;
			while ((currentCharCode = inputStream.read()) != -1) {
				int currentKeyCharCode = key.charAt(currentChar);

				int resultCharacter = currentCharCode + currentKeyCharCode +
						currentChar + currentBlock;

				while (resultCharacter > 127)
					resultCharacter -= 127;

				writer.flush();
				writer.write((char) resultCharacter);

				currentChar++;

				if (currentChar == blockLength) {
					currentChar = 0;
					currentBlock += 1;
				}
			}

			inputStream.close();
			writer.close();
			outputStream.close();
			file.delete();
			ui.notifyFileEncrypted();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean decrypt(File file, String key, UI ui) {
		int blockLength = key.length();
		int currentChar = 0;
		int currentBlock = 0;
		File resultFile = new File(file.getAbsolutePath().replace(".enc", ".txt"));
		Charset charset = Charset.forName("ascii");

		try {
			InputStream inputStream = new FileInputStream(file);
			OutputStream outputStream = new FileOutputStream(resultFile);
			Writer writer = new OutputStreamWriter(outputStream, charset);

			System.out.println(inputStream.available());

			int currentCharCode;
			while ((currentCharCode = inputStream.read()) != -1) {
				int currentKeyCharCode = key.charAt(currentChar);

				int resultCharacter = currentCharCode - currentKeyCharCode - currentChar - currentBlock;
				while (resultCharacter < 0)
					resultCharacter += 127;

				writer.flush();
				writer.write((char) resultCharacter);

				currentChar++;

				if (currentChar == blockLength) {
					currentChar = 0;
					currentBlock += 1;
				}
			}

			inputStream.close();

			writer.close();
			outputStream.close();
			ui.notifyFileDecrypted();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
