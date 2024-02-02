package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.Date;
import java.net.*;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
/**
 * Servlet implementation class myServlet
 */
public class myServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public myServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String inputData = request.getParameter("input");
		System.out.println(inputData);
		String apiKey = "6be99bfe41c7aabe4c1915a26620bb59";
		String apiurl = "urlApi/weather?q="+ inputData + "&appid=" + apiKey;

		
//		API INTEGRATION
		URL url = new URL(apiurl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream ipStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(ipStream);
		Scanner sc = new Scanner(reader);
		StringBuilder resData = new StringBuilder();
		while(sc.hasNext()) {
			resData.append(sc.nextLine());
		}
		System.out.println(resData);
//		Type Casting Or Parsing Into JSON Format.....
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(resData.toString(), JsonObject.class);
		
		long DateTimeStamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(DateTimeStamp).toString();
		double TempKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int tempCelcius =(int)( TempKelvin - 273.15);
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		double speed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
//		Set the Data as Request Attributes. Then the index.jsp will send it as response.
		request.setAttribute("Date", date);
		request.setAttribute("City", inputData);
		request.setAttribute("Temperature", tempCelcius);
		request.setAttribute("humidity", humidity);
		request.setAttribute("speed", speed);
		request.setAttribute("weatherCondition", weatherCondition);
		connection.disconnect();
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

}
