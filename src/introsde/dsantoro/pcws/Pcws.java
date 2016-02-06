package introsde.dsantoro.pcws;

import java.util.Collection;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import introsde.dsantoro.dbws.Activity;
import introsde.dsantoro.dbws.Goal;
import introsde.dsantoro.dbws.Meal;
import introsde.dsantoro.dbws.Person;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL)
public interface Pcws {

	// Person methods
	@WebMethod(operationName="readPerson")
	@WebResult(name="person") 
	public Person readPerson(@WebParam(name="personId") Long id);

	@WebMethod(operationName="readPersonList")
	@WebResult(name="person") 
	public Collection<Person> readPersonList();

	@WebMethod(operationName="createPerson")
	@WebResult(name="person") 
	public Person createPerson(@WebParam(name="personRqst") Person person);

	// Goal methods
	@WebMethod(operationName="readGoal")
	@WebResult(name="goal") 
	public Goal readGoal(@WebParam(name="goalId") Long id);

	@WebMethod(operationName="readGoalList")
	@WebResult(name="goal") 
	public Collection<Goal> readGoalList();

	@WebMethod(operationName="createGoal")
	@WebResult(name="goal") 
	public Goal createGoal(@WebParam(name="goalRqst") Goal goal, @WebParam(name="person") Person person);

	// Activity methods
	@WebMethod(operationName="readActivity")
	@WebResult(name="activity") 
	public Activity readActivity(@WebParam(name="activityId") Long id);

	@WebMethod(operationName="readActivityList")
	@WebResult(name="activity") 
	public Collection<Activity> readActivityList();

	@WebMethod(operationName="createActivity")
	@WebResult(name="activity") 
	public Activity createActivity(@WebParam(name="activityRqst") Activity activity, @WebParam(name="person") Person person);
	
	// Meal methods
	@WebMethod(operationName="readMeal")
	@WebResult(name="meal") 
	public Meal readMeal(@WebParam(name="mealId") Long id);

	@WebMethod(operationName="readMealList")
	@WebResult(name="meal") 
	public Collection<Meal> readMealList();

	@WebMethod(operationName="createMeal")
	@WebResult(name="meal") 
	public Meal createMeal(@WebParam(name="mealRqst") Meal meal, @WebParam(name="person") Person person);
	
	@WebMethod(operationName="searchMeals")
	@WebResult(name="meal") 
	public Collection<Meal> searchMeals(@WebParam(name="searchKey") String searchKey, @WebParam(name="start") int start, @WebParam(name="quantity") int quantity);
	
}
