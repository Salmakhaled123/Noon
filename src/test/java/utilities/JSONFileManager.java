package utilities;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.json.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public class JSONFileManager {
    private final Logger log = LogManager.getLogger(JSONFileManager.class);

    public LinkedHashMap<String,Object>data;

    public JSONFileManager(String filePath)
    {
        try {
            Type type=new TypeToken<LinkedHashMap<String,Object>>(){}.getType();
            data=new Gson().fromJson(new FileReader(filePath),type);
            log.info("data come from json : {}",data.values());

        }catch (Exception e)
        {
            log.error("error in catch in json file: {}",e);

        }
    }
    public Object getValue(String key)
    {
        log.info("key from json file manager : {}",key);
        return  data.get(key);
    }





}
