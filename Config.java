public class Config {
	private List<Microservice> microservices;
	public void setMicroservices(List<Microservice> microservices) {
		this.microservices = microservices;
	}
	public List<Microservice> getMicroservices() {
		return this.microservices;
	}
}