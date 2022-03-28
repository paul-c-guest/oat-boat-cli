package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Handles saving and loading the Database object to a local .db file
 * 
 * @author ballsies
 *
 */
public class DBIO {

	private final String fileName = "oatboat.db";

	/**
	 * Looks for existing db file in local storage
	 * 
	 * @return either the existing Database object or null.
	 */
	public Database load() {
		Database db = null;
		try {
			FileInputStream fis = new FileInputStream(new File(fileName));
			ObjectInputStream ois = new ObjectInputStream(fis);
			db = (Database) ois.readObject();
			ois.close();
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("local database not found... ");
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		}
		return db;
	}

	/**
	 * writes the serialized database object as a local file
	 */
	public void save(Serializable database) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(database);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
