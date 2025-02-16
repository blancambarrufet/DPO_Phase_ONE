package persistance.api;

import business.entities.TeamPrint;
import persistance.PersistenceManager;
import persistance.api.exception.ApiException;
import persistance.json.exceptions.PersistanceException;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;

public class ApiPersistence<T> extends PersistenceManager<T> {
    private final ApiHelper apiHelper;
    private final String apiUrl;
    private final Class<T[]> type;
    private final Gson gson;

    public ApiPersistence(String apiUrl, Class<T[]> type) throws ApiException {
        this.apiHelper = new ApiHelper();
        this.apiUrl = apiUrl;
        this.type = type;
        this.gson = new Gson();
    }

    @Override
    public List<TeamPrint> loadAll() throws PersistanceException {
        try {
            String response = apiHelper.getFromUrl(apiUrl);
            return Arrays.asList(gson.fromJson(response, type));
        } catch (Exception e) {
            throw new PersistanceException("API data retrieval failed.", e);
        }
    }

    @Override
    public void save(List<T> data) throws PersistanceException {
        try {
            String jsonData = gson.toJson(data);
            apiHelper.postToUrl(apiUrl, jsonData);
        } catch (Exception e) {
            throw new PersistanceException("API data saving failed.", e);
        }
    }
}
