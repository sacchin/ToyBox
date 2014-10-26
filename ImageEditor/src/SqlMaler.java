import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class SqlMaler {

	public static void main(String[] args) throws Exception {
		BackupPartyData();
	}

	public static String processID = "20241";
	/**
	 * "‚ğ\"‚Éƒƒ‚’ ‚Å•ÏŠ·‚µ‚½replacedLog‚ğèì‹Æ‚Åì¬
	 * public static String processID‚ğlog‚ÉŠÜ‚Ü‚ê‚éƒ‚ƒm‚É•ÏŠ·‚µ‚ÄÀs
	 * ‚»‚Ì‚Ü‚Üo—Í‚·‚éVŒ^
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void BackupPartyData() throws FileNotFoundException,
			IOException {
		FileReader fr1 = new FileReader(new File("C:/Users/U-U/Desktop/replacedLog.txt"));
		FileWriter fw1 = new FileWriter(new File("C:/Users/U-U/Desktop/insert.txt"));
		BufferedReader br1 = new BufferedReader(fr1);
		BufferedWriter bw1 = new BufferedWriter(fw1);
		PrintWriter pw = new PrintWriter(bw1);

		try {
			System.out.println("JSONArray party = new JSONArray();");
			System.out.println("JSONArray individual = new JSONArray();");
			while(true){
				String line1 = br1.readLine();
				if(line1 == null){
					break;
				}
				String[] lines = line1.split(processID)[1].split(": ");
				
				if(lines[1].contains("member1")){
					System.out.println("party.put(new JSONObject(\"" + lines[1] + "\"));");
					pw.println("party.put(new JSONObject(\"" + lines[1] + "\"));");
				}else{
					lines[1].replaceAll("\"", "\\\"");
					System.out.println("individual.put(new JSONObject(\"" + lines[1] + "\"));");
					pw.println("individual.put(new JSONObject(\"" + lines[1] + "\"));");
				}
			}
		}finally{
			pw.close();
			bw1.close();
			fw1.close();
			br1.close();
			fr1.close();
		}
	}
	
	/**
	 * ã‹L‚ÌprocessID‚ğlog‚ÉŠÜ‚Ü‚ê‚éƒ‚ƒm‚É•ÏŠ·‚µ‚ÄÀs
	 * No’uŠ·‚ğŠÜ‚Ş‹ŒŒ^
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void BackupPartyDataOld() throws FileNotFoundException,
			IOException {
		FileReader fr1 = new FileReader(new File("C:/Users/U-U/Desktop/log.txt"));
		FileWriter fw1 = new FileWriter(new File("C:/Users/U-U/Desktop/log.txt"));
		BufferedReader br1 = new BufferedReader(fr1);
		BufferedWriter bw1 = new BufferedWriter(fw1);
		PrintWriter pw = new PrintWriter(bw1);

		HashMap<String, String[]> pockemonNumber = new HashMap<String,String[]>();
		try {
			System.out.println("JSONArray party = new JSONArray();");
			System.out.println("JSONArray individual = new JSONArray();");
			while(true){
				String line1 = br1.readLine();
				if(line1 == null){
					break;
				}
				String[] lines = line1.split(processID)[1].split(": ");
				String before = lines[1];
				if(lines[1].contains("member1")){
					System.out.println("party.put(new JSONObject(\"" + lines[1] + "\"));");
				}else{
					boolean isOther = false, isAllmost = true;
					if(lines[1].contains("645-2")){
						String[] test = lines[1].split("645-2");
						lines[1] = test[0] + "652" + test[1];
					}else if(lines[1].contains("642-2")){
						String[] test = lines[1].split("642-2");
						lines[1] = test[0] + "649" + test[1];
					}else if(lines[1].contains("641-2")){
						String[] test = lines[1].split("641-2");
						lines[1] = test[0] + "647" + test[1];
					}else if(lines[1].contains("479-6")){
						String[] test = lines[1].split("479-6");
						lines[1] = test[0] + "484" + test[1];
					}else if(lines[1].contains("479-5")){
						String[] test = lines[1].split("479-5");
						lines[1] = test[0] + "483" + test[1];
					}else if(lines[1].contains("479-4")){
						String[] test = lines[1].split("479-4");
						lines[1] = test[0] + "482" + test[1];
					}else if(lines[1].contains("479-3")){
						String[] test = lines[1].split("479-3");
						lines[1] = test[0] + "481" + test[1];
					}else if(lines[1].contains("479-2")){
						String[] test = lines[1].split("479-2");
						lines[1] = test[0] + "480" + test[1];
					}else if(lines[1].contains("\"pockemonNo\":\"645")){
						String[] test = lines[1].split("645");
						lines[1] = test[0] + "652" + test[1];
					}else if(lines[1].contains("\"pockemonNo\":\"644")){
						String[] test = lines[1].split("644");
						lines[1] = test[0] + "651" + test[1];
					}else if(lines[1].contains("\"pockemonNo\":\"643")){
						String[] test = lines[1].split("643");
						lines[1] = test[0] + "650" + test[1];
					}else if(lines[1].contains("\"pockemonNo\":\"642")){
						String[] test = lines[1].split("642");
						lines[1] = test[0] + "648" + test[1];
					}else{
						isOther = true;
						int updateNo = 0;
						for(int i = 718 ; 0 < i ; i--){
							if(lines[1].contains("\"" + i)){
								if(480 <= i && i <= 641){
									updateNo = i + 5;
									String[] test = lines[1].split(String.valueOf(i));		
									lines[1] = test[0] + updateNo + test[1];	
									isAllmost = false;
								}else if(651 <= i){
									updateNo = i + 8;
									String[] test = lines[1].split(String.valueOf(i));		
									lines[1] = test[0] + updateNo + test[1];	
									isAllmost = false;
								}
//								if(updateNo < 10){
//									lines[1] = test[0] + "00" + updateNo + test[1];
//								}else if(9 < updateNo && updateNo < 100){
//									lines[1] = test[0] + "0" + updateNo + test[1];
//								}else{
///								}
								break;
							}
						}
					}
//					if(!isOther || !isAllmost){
//						System.out.println(before);
//					}
					System.out.println("individual.put(new JSONObject(\"" + lines[1] + "\"));");
				}
			}

			//			if(1 < lines.length){
			//			System.out.println("databaseHelper.insertItemData(\"" + lines[0] + "\");");
			//		}
		}finally{
			br1.close();
			fr1.close();
		}
	}
}
