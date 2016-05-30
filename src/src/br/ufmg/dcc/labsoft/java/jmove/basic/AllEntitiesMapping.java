package br.ufmg.dcc.labsoft.java.jmove.basic;

import java.util.AbstractMap;
import java.util.HashMap;

public class AllEntitiesMapping {

	private AbstractMap<Integer, String> allDependeciesMapByID = null;
	private AbstractMap<String, Integer> allDependeciesMapByName = null;
	private static AllEntitiesMapping instance;

	private AllEntitiesMapping() {
		// TODO Auto-generated constructor stub
		this.allDependeciesMapByID = new HashMap<Integer, String>();
		this.allDependeciesMapByName = new HashMap<String, Integer>();
	}

	public static AllEntitiesMapping getInstance() {
		if (instance == null) {
			instance = new AllEntitiesMapping();
		}
		return instance;
	}

	private void insertMapping(String dependency) {
		if (!allDependeciesMapByName.containsKey(dependency)) {
			allDependeciesMapByName.put(dependency,
					allDependeciesMapByName.size());
			allDependeciesMapByID.put(allDependeciesMapByName.get(dependency),
					dependency);
		}
	}

	public String getByID(int id) {
		return allDependeciesMapByID.get(id);
	}

	public Integer getByName(String name) {
		allDependeciesMapByName.get(allDependeciesMapByName.get(name));
		return allDependeciesMapByName.get(name);
	}

	public int createEntityID(String entityName) {
		int entityID;
		if (getInstance().getByName(entityName) != null) {
			entityID = getInstance().getByName(entityName);
		} else {
			getInstance().insertMapping(entityName);
			entityID = getInstance().getByName(entityName);
		}
		return entityID;
	}

}
