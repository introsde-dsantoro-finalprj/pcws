package introsde.dsantoro.pcws;

import java.util.Collection;

import javax.jws.WebService;

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
		return blws.searchFoods(searchKey, start, quantity);
	}
}
