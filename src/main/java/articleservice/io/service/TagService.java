package articleservice.io.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import articleservice.io.bean.Tag;
import articleservice.io.repository.TagRepository;

@Service
public class TagService {

	@Autowired
	TagRepository tagRepository;

	public String getTagCount() {
		List<Tag> tag = tagRepository.findAll();

		Map<String, Integer> map = new HashMap<String, Integer>();

		for (Tag tags : tag) {
			if (map.containsKey(tags.getTagName().toLowerCase())) {
				map.put(tags.getTagName().toLowerCase(), map.get(tags.getTagName().toLowerCase()) + 1);
			} else {
				map.put(tags.getTagName().toLowerCase(), 1);
			}
		}

		List<JSONObject> list = new ArrayList<JSONObject>();

		Map<String, String> hm = new LinkedHashMap<>();

		for (Map.Entry<String, Integer> m : map.entrySet()) {
			hm.put("tag", String.valueOf(m.getKey()));
			hm.put("occurance", String.valueOf(m.getValue()));
			JSONObject json = new JSONObject(hm);
			list.add(json);
		}

		return list.toString();
	}

}
