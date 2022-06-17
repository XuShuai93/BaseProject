package com.adair.core.http.params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;

/**
 * 构造json数据格式参数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/7/19 14:38
 */
public class JsonParamsFactory {

	private JsonParamsFactory() {
	}

	public static final class Builder {
		private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		private final JSONObject dataJson;

		public Builder() {
			dataJson = new JSONObject();
		}

		public Builder addParam(String key, String value) {
			try {
				dataJson.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Builder addParam(String key, long value) {
			try {
				dataJson.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Builder addParam(String key, int value) {
			try {
				dataJson.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Builder addParam(String key, float value) {
			try {
				dataJson.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Builder addParam(String key, double value) {
			try {
				dataJson.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Builder addParam(String key, boolean value) {
			try {
				dataJson.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return this;
		}

		public <T> Builder addArrayParam(String key, List<T> value) {
			JSONArray array = new JSONArray();
			int size;
			if (value != null) {
				size = value.size();
				for (int i = 0; i < size; i++) {
					array.put(value.get(i));
				}
			}
			try {
				dataJson.put(key, array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return this;
		}

		public <T> Builder addArrayParam(String key, T[] value) {
			JSONArray array = new JSONArray();
			int size;
			if (value != null) {
				size = value.length;
				for (int i = 0; i < size; i++) {
					array.put(value[i]);
				}
			}
			try {
				dataJson.put(key, array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return this;
		}

		public String build() {
			return dataJson.toString();
		}
	}
}
