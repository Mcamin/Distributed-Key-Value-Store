package de.tub.ise.ec.kv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSystemKVStore implements KeyValueInterface {

	private String rootDir;

	public FileSystemKVStore() {
		this("." + File.separator + "kv_store");
	}

	public FileSystemKVStore(String rootDir) {
		this.rootDir = rootDir;
	}

	/**
	 * returns a value for a given key
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Object getValue(String key) {
		File f = new File(rootDir + File.separator + key);
		Object value = null;
		try {
			FileInputStream fi = new FileInputStream(f);
			ObjectInputStream oi = new ObjectInputStream(fi);
			value = oi.readObject();
			oi.close();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Retrieving value for key " + key + " failed.");
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * returns a list of all keys
	 */
	@Override
	public List<String> getKeys() {
		List<String> result = new ArrayList<>();

		File dir = new File(rootDir);
		File[] files = dir.listFiles();
		for (File f : files) {
			result.add(f.getName());
		}
		return result;
	}

	/**
	 * stores a key value pair on the file system, whereby the key maps to the file
	 * name and the value to the file's content
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public void store(String key, Serializable value) {
		File f = new File(rootDir + File.separator + key);
		if (!f.isDirectory()) {
			if (!f.isFile()) {
				try {
					File parent = f.getParentFile();
					parent.mkdirs(); // create parent directories
					f.createNewFile();
				} catch (IOException e) {
					System.err.println("File " + f.getAbsolutePath() + " could not be created.");
					e.printStackTrace();
				}
				// update file content
				try {
					FileOutputStream fo = new FileOutputStream(f);
					ObjectOutputStream oo = new ObjectOutputStream(fo);
					oo.writeObject(value);
					oo.close();
				} catch (IOException e) {
					System.err.println("Writing value to file failed for key " + key + ".");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * deletes a key value pair on the file system
	 *
	 * @param key
	 */
	public void delete(String key) {
		File f = new File(rootDir + File.separator + key);
		if (!f.isDirectory()) {
			if (f.isFile()) {
				f.delete();
			}
		}
	}
}
