package introsde.dsantoro.blws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.dsantoro.dbws.Meal;

@XmlRootElement(name="meals")
public class MealStore {

	private List<Meal> data = new ArrayList<Meal>();

	public MealStore () {
	}

	@XmlElement(name="meal")
	public List<Meal> getData() {
		return data;
	}

	public void setData(List<Meal> data) {
		this.data = data;
	}
}