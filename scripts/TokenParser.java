import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenParser {

	public static boolean findAWSToken(File newFile) throws IOException{
		//File newFile = new File(fileName);
		FileInputStream is = new FileInputStream(newFile);			
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String nextLine = new String();

		while((nextLine = br.readLine()) != null) {
			Pattern keyPattern = Pattern.compile("\"([^\"]*)\"");
			Matcher m = keyPattern.matcher(nextLine);
			while(m.find()) {
				String res = m.group(1);
				if(res.matches("^(AK).*{18}") && res.matches("\\w{18}")) {
					System.out.println("Found a matching pattern to AWS Access token. Hence rejecting the commit.");
					return true;
				}
			}
			
		}
		return false;
	}
	
	public static boolean findAWSSecretToken(File newFile) throws IOException{
		//File newFile = new File(fileName);
		FileInputStream is = new FileInputStream(newFile);			
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String nextLine = new String();

		while((nextLine = br.readLine()) != null) {
			Pattern keyPattern = Pattern.compile("\"([^\"]*)\"");
			Matcher m = keyPattern.matcher(nextLine);
			while(m.find()) {
				String res = m.group(1);
				if(res.matches("(\\S{40})")) {
					 System.out.println("Found a matching pattern to AWS Security token. Hence rejecting the commit.");

					return true;
				}
			}
			
		}
		return false;
	}

	public static boolean findDOToken(File newFile) throws IOException {
		//File newFile = new File(fileName);
		FileInputStream is = new FileInputStream(newFile);			
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String nextLine = new String();

		while((nextLine = br.readLine()) != null) {
			Pattern keyPattern = Pattern.compile("\"([^\"]*)\"");
			Matcher m = keyPattern.matcher(nextLine);
			while(m.find()) {
				String res = m.group(1);
				if(res.matches("\\w{64}")) {
					System.out.println("Found a matching pattern to Digital Ocean token. Hence rejecting the commit.");
					return true;
				}
			}
			
		}
		return false;
	}
	
	public static void traverseDirectory(File root) throws IOException {
		//System.out.println(root.getName());
		if(root.isFile()) {
			if(findAWSSecretToken(root) || findAWSToken(root) || findDOToken(root) ) {
    			System.exit(-1);
    		}
		}
		else {
			for(File files : root.listFiles())
		    {
		        if(files.isDirectory())
		        {
		        	traverseDirectory(files);
		        }
		        else
		        {
		        	//System.out.println(files.getName());
		        	if(findAWSSecretToken(files) || findAWSToken(files) || findDOToken(files) ) {
		    			System.exit(-1);
		    		}
		        }
		    }
		}
		
	}

	public static void main(String[] args) throws IOException {
		if(args.length < 1) {
			System.exit(-2);
		}
		String fileName = args[0];
		File root = new File(fileName);
		traverseDirectory(root);
		

	}

}
