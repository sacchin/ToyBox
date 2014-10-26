import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.gmail.sacchin.pokemonlibrary.entity.Type;


public class ConvertFromInertSql {

	public static void main(String[] args) throws Exception {

		FileReader fr1 = new FileReader(new File("C:/Users/U-U/Desktop/Recent/pokemonDatabaseFor6thSql.txt"));
		InputStreamReader fr2 = new InputStreamReader(new FileInputStream("C:/Users/U-U/Desktop/Recent/3.txt"),"UTF-8");

		FileWriter fw1 = new FileWriter(new File("C:/Users/U-U/Desktop/insertPokemon.txt"));
		BufferedReader br1 = new BufferedReader(fr1);
		BufferedReader br2 = new BufferedReader(fr2);
		BufferedWriter bw1 = new BufferedWriter(fw1);
		PrintWriter pw = new PrintWriter(bw1);
		//		INSERT INTO "pokemonInfo" VALUES(1,567,'アーケオス','いわ','ひこう',75,140,65,112,65,110,567,'よわき','-','-',32,'false');
		//databaseHelper.insertPockemonData("004", "ヒトカゲ", "-", 0, 0, 0, 0, 0, 0, "-", "-", "-");
		
		
		ArrayList<String> linesArray1 = new ArrayList<String>();
		try {
			while(true){
				String line1 = br1.readLine();
				if(line1 == null){
					break;
				}
				String lines1 = line1.split("INSERT INTO \"pokemonInfo\" VALUES")[1];
				lines1.replace("(", "");
				lines1.replace(");", "");
				linesArray1.add(lines1);
			}
		}finally{
			br1.close();
			fr1.close();
		}
		
		ArrayList<String> linesArray2 = new ArrayList<String>();
		try {
			while(true){
				String line2 = br2.readLine();
				if(line2 == null){
					break;
				}
				String lines2 = line2.split("databaseHelper.insertPockemonData")[1];
				lines2 = lines2.replace("(", "");
				lines2 = lines2.replace(");", "");
				linesArray2.add(lines2);
			}
		}finally{
			br2.close();
			fr2.close();
		}

		ArrayList<String> outputs = new ArrayList<String>();
		for(String lines1 : linesArray1){
			for(String lines2 : linesArray2){
				String[] block1 = lines1.split(",");
				String[] block2 = lines2.split(",");
		
				int type1 = Type.convertSkillNameToNo(block1[3].replace("'", ""));
				int type2 = Type.convertSkillNameToNo(block1[4].replace("'", ""));
				float weight = Float.parseFloat(block1[15]);



				if(Integer.parseInt(block1[1]) < 10){
					if(("\"00" + block1[1] + "\"").equals(block2[0])){
						String output = "databaseHelper.insertPockemonData(" + 
								block2[0] + "," + 
								block2[1] + "," + 
								block2[2] + "," + 
								block2[3] + "," + 
								block2[4] + "," + 
								block2[5] + "," + 
								block2[6] + "," + 
								block2[7] + "," + 
								block2[8] + "," + 
								block2[9] + "," + 
								block2[10] + "," + 
								block2[11] + "," + 
								type1 + "," + 
								type2 + "," + 
								weight + ");";
						outputs.add(output);
					}
				}else if(Integer.parseInt(block1[1]) < 100){
					if(("\"0" + block1[1] + "\"").equals(block2[0])){
						String output = "databaseHelper.insertPockemonData(" + 
								block2[0] + "," + 
								block2[1] + "," + 
								block2[2] + "," + 
								block2[3] + "," + 
								block2[4] + "," + 
								block2[5] + "," + 
								block2[6] + "," + 
								block2[7] + "," + 
								block2[8] + "," + 
								block2[9] + "," + 
								block2[10] + "," + 
								block2[11] + "," + 
								type1 + "," + 
								type2 + "," + 
								weight + ");";
						outputs.add(output);
					}
				}else{
					if(block1[1].equals(block2[0])){
						String output = "databaseHelper.insertPockemonData(\"" + 
								block2[0] + "\"," + 
								block2[1] + "," + 
								block2[2] + "," + 
								block2[3] + "," + 
								block2[4] + "," + 
								block2[5] + "," + 
								block2[6] + "," + 
								block2[7] + "," + 
								block2[8] + "," + 
								block2[9] + "," + 
								block2[10] + "," + 
								block2[11] + "," + 
								type1 + "," + 
								type2 + "," + 
								weight + ");";
						outputs.add(output);
					}
				}
			}
		}
		Collections.sort(outputs);
		
		for(String temp : outputs){
			System.out.println(temp);
			pw.println(temp);
		}

		pw.close();
		bw1.close();
		fw1.close();
	}
}
