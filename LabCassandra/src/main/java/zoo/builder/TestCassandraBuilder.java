package zoo.builder;

import com.datastax.oss.driver.api.core.CqlSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TestCassandraBuilder {
	public static void main(String[] args) {
		try (CqlSession session = CqlSession.builder().build()) {
			KeyspaceBuilderManager keyspaceManager = new KeyspaceBuilderManager(session, "zoo");
			keyspaceManager.dropKeyspace();
			keyspaceManager.selectKeyspaces();
			keyspaceManager.createKeyspace();
			keyspaceManager.useKeyspace();
			
			AnimalsTableBuilderManager tableManager = new AnimalsTableBuilderManager(session);
			tableManager.createTable();
			tableManager.insertIntoTable();
			Scanner myInput = new Scanner( System.in );
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			int choice = 0;
			do{
				System.out.println("[1] Add new animal");
				System.out.println("[2] Get all animals");
				System.out.println("[3] Delete animal by ID");
				System.out.println("[4] Update animal age by ID");
				System.out.println("[5] Select animal by ID");
				System.out.println("[6] Calculate average age of animals");
				System.out.println("[7] Quit");
				choice = myInput.nextInt();
				if (choice == 1){
					System.out.println("ID:");
					int id = myInput.nextInt();
					System.out.println("Animal name:");
					String name =  reader.readLine();
					System.out.println("Animal species:");
					String species =  reader.readLine();
					System.out.println("Age:");
					int age = myInput.nextInt();
					System.out.println("Sektor:");
					String sector =  reader.readLine();
					System.out.println("Catwalk number:");
					String catwalk =  reader.readLine();
					System.out.println("Cage number:");
					String cage =  reader.readLine();
					System.out.println("Awards:");
					String award =  reader.readLine();
					tableManager.insertIntoTableComplex(id, name, species, age, sector, catwalk, cage, award);
				}
				if (choice == 2){
					tableManager.selectFromTable();
				}
				if (choice == 3){
					System.out.println("Select ID to delete by");
					int id = myInput.nextInt();
					tableManager.deleteFromTable(id);
				}
				if (choice == 4){
					System.out.println("Select ID to update  by");
					int id = myInput.nextInt();
					System.out.println("Select age to update");
					int age = myInput.nextInt();
					tableManager.updateIntoTable(id, age);
				}
				if (choice == 5){
					System.out.println("Select ID to view animal by");
					int id = myInput.nextInt();
					tableManager.selectFromTableID(id);
				}
				if (choice == 6){
					tableManager.countAverageAge();
				}
			}while(choice != 7);
			tableManager.dropTable();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
