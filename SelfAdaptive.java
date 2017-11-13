public class SelfAdaptive {
    Monitor monitor = new Monitor();
    RDM rdm = new RDM();
    List<File> wrdl = getWRDL();
    public void adapte(Request request, List<Microservice> services){
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Microservice m : services) {
            Map<String, String> paramMap = m.getParam();
            List<String> paramValues = new ArrayList<String>();
            boolean isContain = false;
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (parameterMap.containsKey(key)) {
                    String[] valueTemp = parameterMap.get(key);
                    paramValues.add(valueTemp[0]);
                    isContain = true;
                }
            }
            if(!isContain) {
                monitor.feedback();
                if(adaptiveMode == "manual") {
                    cooperate(request);
                }else if(adaptiveMode == "assistant") {
                    RDF rdf = rdm.create(paramMap);
                    Attributes attributes = rdf.reasoning();
                    wrdl.inject(attributes);
                    cooperate(request);
                }
            }
        }
    }
    public List<File> getWRDL() {
		List<File> files = new ArrayList<File>();
		String[] paths = new String[]{"browse.xml","delete.xml","insert.xml","update.xml","import.xml","export.xml","other.xml"};
		for (String path : paths) {
			files.add(new File(path));
		}
		return files;
	}
}