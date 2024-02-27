package webserver.util;

import java.util.HashMap;
import java.util.Map;

public class FormData {
    private Map<String, String> values;

    private FormData() {
    }
    public FormData(Map<String, String> values) {}
    public FormData(char[] body) {
        setValues(getValuesFromBytes(body));
    }
    public static FormData getFormDataFromBytes(char[] body) {
        FormData formData = new FormData();
        formData.setValues(getValuesFromBytes(body));
        return formData;
    }

    private static Map<String, String> getValuesFromBytes(char[] body) {
        HashMap<String, String> vals = new HashMap<>();
        String str = new String(body);
        String[] tokens = str.split("&");
        for (String val : tokens) {
            String[] kv = val.split("=");
            String key = kv[0];
            String value = kv[1];

            vals.put(key, value);
        }
        return vals;
    }

    private void setValues(Map<String, String> values) {
        this.values = values;
    }

    public String getValue(String key) {
        return this.values.get(key);
    }

}
