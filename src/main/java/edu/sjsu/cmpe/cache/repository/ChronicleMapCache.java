package edu.sjsu.cmpe.cache.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import net.openhft.chronicle.map.ChronicleMapBuilder;
import edu.sjsu.cmpe.cache.domain.Entry;

public class ChronicleMapCache implements CacheInterface{
	private static String tmp = System.getProperty("java.io.tmpdir");
    private static String pathname = tmp+"/";
    private File file;
	private ConcurrentMap<Long, Entry> map; 
	
	public ChronicleMapCache(String filename) {
		ChronicleMapBuilder<Long, Entry> builder = ChronicleMapBuilder.of(Long.class, Entry.class)
				.entries(100);

		String temp = pathname+filename+".txt";
		System.out.println("**** filename "+temp);
		file = new File(temp);
		
		try{
			map = builder.createPersistedTo(file);
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
	
	}
	
	@Override
	public Entry save(Entry newEntry) {
		checkNotNull(newEntry, "newEntry instance must not be null");
		map.put(newEntry.getKey(), newEntry);
		return newEntry;
	}

	@Override
	public Entry get(Long key) {
		checkArgument(key > 0,
                "Key was %s but expected greater than zero value", key);
		
		return map.get(key);
	}

	@Override
	public List<Entry> getAll() {
		return new ArrayList<Entry>(map.values());
	}

}
