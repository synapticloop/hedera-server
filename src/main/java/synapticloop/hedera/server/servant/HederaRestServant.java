package synapticloop.hedera.server.servant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import synapticloop.nanohttpd.router.RestRoutable;
import synapticloop.nanohttpd.utils.HttpUtils;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public class HederaRestServant extends RestRoutable {

	public HederaRestServant(String routeContext, ArrayList<String> params) {
		super(routeContext, params);
	}

	private String getRestParams(HashMap<String, String> restParams) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Iterator<String> iterator = restParams.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			stringBuilder.append(key + ":" + restParams.get(key));
		}
		return(stringBuilder.toString());
	}

	private Response doMethod(String method, HashMap<String, String> restParams, String unmappedParams) {
		return(HttpUtils.okResponse(this.getClass().getName() + " [ " + method + " ] request: says OK, with params: " + getRestParams(restParams) + ", and un-mapped params of:" + unmappedParams));
	}

	/**
	 * do a get request for a specific file
	 */
	public Response doGet(File rootDir, IHTTPSession httpSession, HashMap<String, String> restParams, String unmappedParams) {
		System.out.println(unmappedParams);
		return(doMethod("GET", restParams, unmappedParams));
	}

	public Response doPost(File rootDir, IHTTPSession httpSession, HashMap<String, String> restParams, String unmappedParams) {
		return(doMethod("POST", restParams, unmappedParams));
	}

	public Response doPut(File rootDir, IHTTPSession httpSession, HashMap<String, String> restParams, String unmappedParams) {
		String replaceAll = unmappedParams.replaceAll("\\.\\./", "").replaceAll("//", "/");
		File file = new File("./artifacts/" + replaceAll);
		file.getParentFile().mkdirs();
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			inputStream = httpSession.getInputStream();
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException ioex) {
			// do something
		} finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException ioex) { }
			}
		}
		System.out.println("PUTTING " + replaceAll);
		return(doMethod("PUT", restParams, unmappedParams));
	}

	public Response doHead(File rootDir, IHTTPSession httpSession, HashMap<String, String> restParams, String unmappedParams) {
		return(HttpUtils.okResponse("18276876"));
	}
}
