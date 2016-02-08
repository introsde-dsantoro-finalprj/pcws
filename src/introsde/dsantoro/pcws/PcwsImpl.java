package introsde.dsantoro.pcws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import org.json.JSONObject;

import introsde.dsantoro.blws.Blws;
import introsde.dsantoro.dbws.Activity;
import introsde.dsantoro.dbws.Goal;
import introsde.dsantoro.dbws.Meal;
import introsde.dsantoro.dbws.Person;
import introsde.dsantoro.storagews.Storagews;

@WebService(endpointInterface = "introsde.dsantoro.pcws.Pcws", serviceName="pcwsService")
public class PcwsImpl implements Pcws {
	private Storagews storagews;
	private Blws blws;

	public PcwsImpl(Storagews storagews, Blws blws) {
		this.storagews = storagews;
		this.blws = blws;
	}

	@Override
	public Person readPerson(Long id) {
		return storagews.readPerson(id);
	}

	@Override
	public Collection<Person> readPersonList() {
		return storagews.readPersonList();	
	}

	@Override
	public Person createPerson(Person person) {
		return storagews.createPerson(person);
	}

	@Override
	public Goal readGoal(Long id) {
		return storagews.readGoal(id);
	}

	@Override
	public Collection<Goal> readGoalList() {
		return storagews.readGoalList();
	}

	@Override
	public Goal createGoal(Goal goal, Person person) {
		return storagews.createGoal(goal, person);
	}

	@Override
	public Activity readActivity(Long id) {
		return storagews.readActivity(id);
	}

	@Override
	public Collection<Activity> readActivityList() {
		return storagews.readActivityList();
	}

	@Override
	public Activity createActivity(Activity activity, Person person) {
		return storagews.createActivity(activity, person);
	}

	@Override
	public Meal readMeal(Long id) {
		return storagews.readMeal(id);
	}

	@Override
	public Collection<Meal> readMealList() {
		return storagews.readMealList();
	}

	@Override
	public Meal createMeal(Meal meal, Person person) {
		return storagews.createMeal(meal, person);
	}

	@Override
	public Collection<Meal> searchMeals(String searchKey, int start, int quantity) {
		return storagews.searchMeals(searchKey, start, quantity);		
	}

	@Override
	public String checkGoal(Long personId) {
		Person person = storagews.readPerson(personId);		
		JSONObject personCalSummary = getJsonCaloriesSummary(person);
		return blws.checkGoal(personCalSummary).toString();		
	}

	private JSONObject getJsonCaloriesSummary(Person person) {

		ArrayList<Integer> takenList = new ArrayList<Integer>();
		ArrayList<Integer> burnedList = new ArrayList<Integer>();		
		Calendar today = Calendar.getInstance();
		today.setTime(new Date());
		// Calories taken
		Iterator<Meal> im = person.getMeals().getMeal().iterator();	
		if (im.hasNext()) {
			while(im.hasNext()){
				Meal m = im.next();				
				if (sameDay(m.getDatetime(),today)){
					takenList.add(m.getCalories());
				}
			}
		}

		// Calories burned		
		Iterator<Activity> ia = person.getActivities().getActivity().iterator();	
		if (ia.hasNext()) {
			while(ia.hasNext()){
				Activity a = ia.next();				
				if (sameDay(a.getDatetime(),today)){
					burnedList.add(a.getCalories());
				}
			}
		}
		burnedList.add(person.getDaycalories());

		// Today Goal
		Integer todayGoal = 0;
		Iterator<Goal> ig = person.getGoals().getGoal().iterator();	
		if (ig.hasNext()) {
			while(ig.hasNext()){
				Goal g = ig.next();				
				if (sameDay(g.getDay(),today)){
					todayGoal = g.getCalories();
				}
			}
		}

		JSONObject caloriesSummary = new JSONObject();
		caloriesSummary.put("todayGoal", todayGoal);
		caloriesSummary.put("calTaken", takenList);
		caloriesSummary.put("calBurned", burnedList);
		return caloriesSummary;
	}

	private boolean sameDay(XMLGregorianCalendar d1, Calendar d2) {
		return ( (d1.getYear() == d2.get(Calendar.YEAR)) && (d1.getMonth() == d2.get(Calendar.MONTH)+1) && (d1.getDay() == d2.get(Calendar.DAY_OF_MONTH)) );
	}
}
