package synapticloop.hedera.server.servant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import synapticloop.nanohttpd.router.RestRoutable;
import synapticloop.nanohttpd.utils.HttpUtils;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public class HederaRestServant extends RestRoutable {

	public HederaRestServant(String routeContext, ArrayList<String> params) {
		super(routeContext, params);
	}

	/**
	 * do a get request for a specific file
	 */
	public Response doGet(File rootDir, IHTTPSession httpSession, HashMap<String, String> restParams, String unmappedParams) {
		String replaceAll = unmappedParams.replaceAll("\\.\\./", "").replaceAll("//", "/");
		System.out.println("GETTING " + replaceAll);
		File file = new File("./artifacts/" + replaceAll);
		try {
			InputStream inputStream = new FileInputStream(file);
			return(new Response(Response.Status.OK, "application/octet-stream", inputStream));
		} catch (FileNotFoundException fnfex) {
			return(HttpUtils.notFoundResponse());
		}
	}

	public Response doPut(File rootDir, IHTTPSession httpSession, HashMap<String, String> restParams, String unmappedParams) {
		String replaceAll = unmappedParams.replaceAll("\\.\\./", "").replaceAll("//", "/");
		System.out.println("PUTTING " + replaceAll);

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
		System.out.println("DONE " + replaceAll);
		return(HttpUtils.okResponse());
	}

	public Response doHead(File rootDir, IHTTPSession httpSession, HashMap<String, String> restParams, String unmappedParams) {
		return(HttpUtils.okResponse("18276876"));
	}
}
