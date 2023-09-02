package io.unfish;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import io.unfish.Pair;
import java.util.List;
import java.util.ArrayList;

public class txt {

    public static Map<String, String> book = new HashMap<>();
    public static Map<String, Pair> sublist = new HashMap<>();
    public static List<String> cflines = new ArrayList<>();
    public static File conf = null;
    public static boolean debug = false;
    public String fformat(String fname){
        String format = "";
        for(int i = 0; i < fname.length(); ++i){
            char ch = fname.charAt(i);
            if(ch == '.'){
                format = "";
                continue;
            }
            format += ch;

        }
        return format;
    }
    private void clndata() {
    	book.clear();
    	sublist.clear();
    	cflines.clear();
    }
    private void open() {
    	File f = conf;
        if(!f.exists()){
            System.out.println("The file " + f.getAbsolutePath() + " does not exist [function: txt() in package io.unfish]");
            return;
        }
        String format = fformat(f.getName());
        if(format.equals("txt")){
            try {
            	conf = f;
                Scanner sc = new Scanner(f);
                String groups[] = new String[20];
                int currentgroup = 0;
                groups[0] = "public";
                Map<String,List<String>> group_elements = new HashMap<>();
                Map<Integer, String> paths = new HashMap<>();
                List<String> _a = new ArrayList<>();
                paths.put(0,"public");
                group_elements.put("public",_a);
                Pair _b = new Pair();
                _b.first = 0;
                sublist.put("public",_b);
                for(int currentline = 1 ;sc.hasNextLine(); ++currentline ){

                    String line = sc.nextLine();
                    cflines.add(line);
                    if(!line.contains(":")){

                        if(line.trim().equals("}") || line.trim().equals(".")
                        		|| !sc.hasNextLine()){
                        	if(line.trim().length() > 1)
                        		group_elements.get(paths.get(currentgroup)).add(line);
                        	
                            sublist.get(paths.get(currentgroup)).second = currentline;
                            String compile_gr_elements = "";
                            for(String str: group_elements.get(paths.get(currentgroup))){
                                compile_gr_elements += str + "\n";
                            }
                            book.put(paths.get(currentgroup),compile_gr_elements);
                            //System.out.println(paths.get(currentgroup) + " " + compile_gr_elements);
                            --currentgroup;
                            continue;
                        }
                        else
                            group_elements.get(paths.get(currentgroup)).add(line);
                    }
                    else{
                     String key = "", value = "";
                     for(int i = 0, j = 0; i < line.length(); ++i){
                         char ch = line.charAt(i);
                         if(ch == ':' && j != 1){
                             j = 1;
                             continue;
                         }
                         if(j == 0){
                             key += ch;
                         }
                         if(j == 1){
                             value += ch;
                         }
                     }
                     String path = "";
                     for(int i = 0; i <= currentgroup; ++i){
                         path += groups[i] + "#";
                     }
                     path += key;
                      if(value.length() == 0 || value.trim().equals("{")){
                        groups[++currentgroup] = key;
                        Pair pair = new Pair();
                        pair.first = currentline;
                        sublist.put(path, pair);
                        List<String> gr_element_list = new ArrayList<>();
                        group_elements.put(path,gr_element_list);
                        paths.put(currentgroup,path);
                        continue;
                    }
                    else{
                        value = value.trim();
                        group_elements.get(paths.get(currentgroup)).add(line);
                        book.put(path,value);
                        Pair _p = new Pair();
                        _p.first = currentline;
                        _p.second = currentline;
                        sublist.put(path, _p);
                        //System.out.println(path +" "  + value);
                     }
                    }
                }
                sc.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Format " + format + " is unsupported [function: txt() in package io.unfish]");
        }
    }
    public txt(Path p){
        conf = p.toFile();
        open();
    }
    private void write_cf() {
    	final List<String> currnt_cflines = cflines;
    	try {
    	PrintWriter pw = new PrintWriter(conf);
    	for(String li : currnt_cflines) {
    		pw.println(li);
    		if(debug)
    		System.out.println(" []  " + li);
    		pw.flush();
    	}
    	pw.close();
    	clndata();
    	open();
    	}catch(IOException e) {
    		
    	}
    	
    }
    public void set(String option, String value0, List<String> value1, boolean ifnex_crNew) {
    	String path_components[] = option.split("#");
    	String key = path_components[path_components.length - 1];
    	if(sublist.containsKey(option)) {
    		
    		Pair _b = sublist.get(option);
    		if(_b.first == _b.second) {
    			if(value1 != null) {
    				cflines.set((int)_b.first - 1, key + ":");
    				cflines.add((int)_b.first, ".");
    				cflines.addAll((int)_b.first, value1);
    				write_cf();
    			}
    			else if(!value0.isBlank()){
    				if(debug)
    				for(String line: cflines) {
    					
    					System.out.println(" {1}  " + line);
    				}
    				System.out.println(cflines.set((int)(_b.first) -1, key + ": " + value0));
    				if(debug)
    				for(String line: cflines) {
    					System.out.println(" {2}  " + line);
    				}
    				write_cf();
    			}
    		}
    		else {
    			List<String> value2 = new ArrayList<>();
    			if(value0 != null && value0.length() > 0) {
    				value2.add(value0);
    			}
    			if(value1 != null) {
    				value2.addAll(value1);
    			}
    			int i = 0;
    			if(debug)
    			for(String li: cflines) {
    				System.out.println(" {2.5}  " + li);
    			}
    			if((int)_b.second - (int)_b.first > 1)
    			for( ; i < (int)_b.second - (int)_b.first - 1&& i < value2.size(); ++i) {
    				cflines.set((int)_b.first + i,value2.get(i));
    			}
    			if(i == value2.size() && i < (int)_b.second - (int)_b.first - 1) {
    				for(; i < (int)_b.second - (int)_b.first - 1; ++i) {
    					cflines.remove((int)_b.first + i);
    				}
    			}
    			for(; i < value2.size(); ++i) {
    				cflines.add((int)_b.first + i, value2.get(i));
    			}
    			if(debug)
    			for(String li: cflines) {
    				System.out.println(" {4}  " + li);
    			}
    			write_cf();
    		}
    	}
    	else if(ifnex_crNew){
    		if(option.contains("#"))
    		{
    			String group_path = "";
    			for(int a = 0; a < path_components.length - 1; ++a) {
    			String currnt_group = path_components[a];
    			String path = "";
    			String parent_group_path = "";
    			for(int b = 0; b < a; ++b) {
    				path += path_components[b] + "#";
    			}
    			if(a > 0)
    			parent_group_path = path.substring(0,path.length() - 1);
    			path += currnt_group;
    			group_path = path;
    			if(!sublist.containsKey(path)) {
    				if(a == 0 || !sublist.containsKey(parent_group_path)) {
    					parent_group_path = "public";
    				}
    				Pair _p = sublist.get(parent_group_path);
    				cflines.add((int)_p.first, ".");
    				cflines.add((int)_p.first,currnt_group + ":");
    				write_cf();
    				continue;
    			}
    		}
    			System.out.println(group_path);
    			Pair _p = sublist.get(group_path);
    			if(value0 != null && value0.length() > 0)
    			cflines.add((int)_p.first, key + ": " + value0);
    			if(value1 != null) {
    				cflines.add((int)_p.first, ".");
    				cflines.addAll((int)_p.first, value1);
    				cflines.add((int)_p.first, key + ":");
    			}
    			write_cf();
    		}
    		else
    		{
    		if(value0 != null && value0.length() > 0)
    			cflines.add(1,option + ": " + value0);
    		if(value1 != null) {
    			cflines.add(1, ".");
    			cflines.addAll(1, value1);
    			cflines.add(1, key);
    		}
    		write_cf();	
    		}
    		
    	}
    }
}
