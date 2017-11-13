public class Profile {
	private String url;
	private String method;
	public Profile(String url，String method) {
		this.url = url;
		this.method = method;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return this.url;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMethod() {
		return this.method;
	}
	public Config lookup(MemoryPool memoryPool) {
		Config config = memoryPool.search(this.getUrl, this.getMethod);
		return config;
	}
}