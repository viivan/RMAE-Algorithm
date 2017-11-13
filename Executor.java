public class Executor {
	public Response doMicroservices(List<Microservice> services) {
		for(Microservice m : services) {
			m.execute();
		}
	}
}