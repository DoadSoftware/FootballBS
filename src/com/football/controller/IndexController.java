package com.football.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser.Feature;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.broadcaster.Football;
import com.football.containers.Scene;
import com.football.containers.ScoreBug;
import com.football.service.FootballService;
import com.football.util.FootballFunctions;
import com.football.util.FootballUtil;
import com.football.model.Clock;
import com.football.model.Configurations;
import com.football.model.Event;
import com.football.model.EventFile;
import com.football.model.Match;

import net.sf.json.JSONObject;

@Controller
public class IndexController 
{
	@Autowired
	FootballService footballService;
	
	public static String expiry_date = "2024-12-31";
	public static String current_date = "";
	public static String error_message = "";
	public static Football this_Football;
	public static Match session_match;
	public static EventFile session_event;
	public static Clock session_clock = new Clock();
	public static Configurations session_Configurations;
	public static  Match SwapMatch = new Match();
	public static int counter;
	
	List<Scene> session_selected_scenes = new ArrayList<Scene>();
	public static String session_selected_broadcaster;
	public static Socket session_socket;
	public static PrintWriter print_writer;
	
	public static ObjectMapper objectMapper = new ObjectMapper();
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model) throws JAXBException, IOException, ParseException 
	{		
//		if(current_date == null || current_date.isEmpty()) {
//			current_date = KabaddiFunctions.getOnlineCurrentDate();
//		}

		model.addAttribute("session_viz_scenes", new File(FootballUtil.FOOTBALL_DIRECTORY + 
				FootballUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".via") && pathname.isFile();
		    }
		}));

		model.addAttribute("match_files", new File(FootballUtil.FOOTBALL_DIRECTORY 
				+ FootballUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".json") && pathname.isFile();
		    }
		}));
		
		if(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CONFIGURATIONS_DIRECTORY + "BigScreen.xml").exists()) {
			session_Configurations = (Configurations)JAXBContext.newInstance(Configurations.class).createUnmarshaller().unmarshal(
				new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CONFIGURATIONS_DIRECTORY + "BigScreen.xml"));
		} else {
			session_Configurations = new Configurations();
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
				new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CONFIGURATIONS_DIRECTORY + "BigScreen.xml"));
		}
		
		model.addAttribute("session_Configurations",session_Configurations);
	
		return "initialise";
	}
	
	@RequestMapping(value = {"/match"}, method = {RequestMethod.POST,RequestMethod.GET})
	public String footballMatchPage(ModelMap model, 
		@RequestParam(value = "selectedBroadcaster", required = false, defaultValue = "") String selectedBroadcaster,
		@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddresss,
		@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") String vizPortNumber)
			throws IOException, ParseException, JAXBException, InterruptedException  
	{
		model.addAttribute("match_files", new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".json") && pathname.isFile();
		    }
		}));

//		model.addAttribute("licence_expiry_message",
//			"Software licence expires on " + new SimpleDateFormat("E, dd MMM yyyy").format(
//			new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));
		
		objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		
		session_match = new Match();
		session_event = new EventFile();
		if(session_event.getEvents() == null || session_event.getEvents().size() <= 0)
			session_event.setEvents(new ArrayList<Event>());
		
		session_Configurations.setBroadcaster(selectedBroadcaster);
		session_Configurations.setIpAddress(vizIPAddresss);
		
		if(!vizPortNumber.trim().isEmpty()) {
			session_Configurations.setPortNumber(Integer.valueOf(vizPortNumber));
			session_socket = new Socket(vizIPAddresss, Integer.valueOf(vizPortNumber));
			print_writer = new PrintWriter(session_socket.getOutputStream(), true);
		}
		session_selected_broadcaster = selectedBroadcaster;
		
		switch (session_selected_broadcaster.toUpperCase()) {
		case "FOOTBALL":
			session_selected_scenes.add(new Scene("D:\\DOAD_In_House_Everest\\Everest_Sports\\Everest_Intercontinental_2024\\Scene\\BigScreen_New.sum",FootballUtil.ONE)); // Front layer
			//session_selected_scenes.add(new Scene(KabaddiUtil.BG_SCENE_PATH,KabaddiUtil.THREE));
			session_selected_scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 0 ;");
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In START;");
			//session_selected_scenes.get(1).scene_load(print_writer, session_selected_broadcaster);
			this_Football = new Football();
			this_Football.scorebug = new ScoreBug();
			break;
		}
		
		JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
			new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CONFIGURATIONS_DIRECTORY + "BigScreen.xml"));
		
		model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
		model.addAttribute("session_match", session_match);
		model.addAttribute("session_event", session_event);
		
		return "match";
	}
	
	@RequestMapping(value = {"/processFootballProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processFootballProcedures(
			@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
			@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess)
					throws JAXBException, IllegalAccessException, InvocationTargetException, IOException, NumberFormatException, InterruptedException
	{	
		Event this_event = new Event();
		if(session_selected_broadcaster != null && !session_selected_broadcaster.equalsIgnoreCase(FootballUtil.EURO_LEAGUE)) {
			if(!whatToProcess.equalsIgnoreCase(FootballUtil.LOAD_TEAMS)) {
				if(valueToProcess.contains(",")) {
					if(session_match.getMatchFileName() == null || session_match.getMatchFileName().isEmpty()) {
						session_match = new ObjectMapper().readValue(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + 
								session_match.getMatchFileName()), Match.class);
						session_event = new ObjectMapper().readValue(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + 
								session_match.getMatchFileName()), EventFile.class);
						session_match.setEvents(session_event.getEvents());
						session_match = FootballFunctions.populateMatchVariables(footballService,session_match);
					}
				}
			}
		}
		switch (whatToProcess.toUpperCase()) {
		case FootballUtil.LOG_STAT:

			if(valueToProcess.toUpperCase().contains(FootballUtil.PENALTIES)) {
				if(valueToProcess.split("_")[1].toUpperCase().contains(FootballUtil.INCREMENT)) {
					if(valueToProcess.split("_")[0].toUpperCase().contains(FootballUtil.HOME)) {
						if(valueToProcess.split("_")[3].toUpperCase().contains(FootballUtil.HIT)) {
							session_match.setHomePenaltiesHits(session_match.getHomePenaltiesHits() + 1);
						}else if(valueToProcess.split("_")[3].toUpperCase().contains(FootballUtil.MISS)) {
							session_match.setHomePenaltiesMisses(session_match.getHomePenaltiesMisses() + 1);
						}
					}else if(valueToProcess.split("_")[0].toUpperCase().contains(FootballUtil.AWAY)) {
						if(valueToProcess.split("_")[3].toUpperCase().contains(FootballUtil.HIT)) {
							session_match.setAwayPenaltiesHits(session_match.getAwayPenaltiesHits() + 1);
						}else if(valueToProcess.split("_")[3].toUpperCase().contains(FootballUtil.MISS)) {
							session_match.setAwayPenaltiesMisses(session_match.getAwayPenaltiesMisses() + 1);
						}
					}
				}else if(valueToProcess.split("_")[1].toUpperCase().contains(FootballUtil.DECREMENT)) {
					if(valueToProcess.split("_")[0].toUpperCase().contains(FootballUtil.HOME)) {
						if(valueToProcess.split("_")[3].toUpperCase().contains(FootballUtil.HIT)) {
							if(session_match.getHomePenaltiesHits() > 0) {
								session_match.setHomePenaltiesHits(session_match.getHomePenaltiesHits() - 1);
							}
						}else if(valueToProcess.split("_")[3].toUpperCase().contains(FootballUtil.MISS)) {
							if(session_match.getHomePenaltiesMisses() > 0) {
								session_match.setHomePenaltiesMisses(session_match.getHomePenaltiesMisses() - 1);
							}
						}
					}else if(valueToProcess.split("_")[0].toUpperCase().contains(FootballUtil.AWAY)) {
						if(valueToProcess.split("_")[3].toUpperCase().contains(FootballUtil.HIT)) {
							if(session_match.getAwayPenaltiesHits() > 0) {
								session_match.setAwayPenaltiesHits(session_match.getAwayPenaltiesHits() - 1);
							}
						}else if(valueToProcess.split("_")[3].toUpperCase().contains(FootballUtil.MISS)) {
							if(session_match.getAwayPenaltiesMisses() > 0) {
								session_match.setAwayPenaltiesMisses(session_match.getAwayPenaltiesMisses() - 1);
							}
						}
					}
				}
			}
			
			new ObjectMapper().writeValue(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + 
					session_match.getMatchFileName()), session_match);
			
//			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
//					new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			
			switch (session_selected_broadcaster) {
			case "FOOTBALL":
				this_Football.ProcessGraphicOption(whatToProcess, session_match, session_clock,SwapMatch, footballService, 
						print_writer, session_selected_scenes, valueToProcess);
				break;
			}
			//session_i_league.ProcessGraphicOption(whatToProcess,session_match, session_clock,footballService,session_socket, session_selected_scenes, valueToProcess);
			return JSONObject.fromObject(session_match).toString();
		case FootballUtil.LOAD_MATCH:
			objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
			
//			session_match = FootballFunctions.populateMatchVariables(footballService, (Match) JAXBContext.newInstance(Match.class).createUnmarshaller()
//					.unmarshal(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + valueToProcess)));
			
			session_match = new ObjectMapper().readValue(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + 
					valueToProcess), Match.class);
			
			if(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + valueToProcess).exists()) {
//				session_event = (EventFile) JAXBContext.newInstance(EventFile.class).createUnmarshaller().unmarshal(
//						new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + valueToProcess));
				
				session_event = new ObjectMapper().readValue(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + 
						valueToProcess), EventFile.class);
			} else {
				session_event = new EventFile();
				//new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + valueToProcess).createNewFile();
				new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + valueToProcess).createNewFile();
			}
			
			session_match.setEvents(session_event.getEvents());
			
			if(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CLOCK_JSON).exists()) {
//				session_match.setClock((Clock) JAXBContext.newInstance(Clock.class).createUnmarshaller().unmarshal(
//						new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CLOCK_XML)));
				
				session_clock = new ObjectMapper().readValue(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CLOCK_JSON), Clock.class);
				session_match.setClock(session_clock);
			} else {
				session_match.setClock(new Clock());
			}
			return JSONObject.fromObject(session_match).toString();

		case "READ_CLOCK":
			
			try {
				counter++;
				
				if(counter == 15) {
					
					if(session_Configurations.getPortNumber() != 0) {
						session_socket = new Socket(session_Configurations.getIpAddress(), Integer.valueOf(session_Configurations.getPortNumber()));
						print_writer = new PrintWriter(session_socket.getOutputStream(), true);
					}
					counter = 0;
				}
				
				if(session_match != null && !valueToProcess.equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(
						new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()).lastModified())))
				{
					session_match = new ObjectMapper().readValue(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + 
							session_match.getMatchFileName()), Match.class);
					
//					session_match = FootballFunctions.populateMatchVariables(footballService, (Match) JAXBContext.newInstance(Match.class).createUnmarshaller()
//							.unmarshal(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + session_match.getMatchFileName())));
					
					if(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + session_match.getMatchFileName()).exists()) {
						
						session_event = new ObjectMapper().readValue(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + 
								session_match.getMatchFileName()), EventFile.class);
						
//						session_event = (EventFile) JAXBContext.newInstance(EventFile.class).createUnmarshaller().unmarshal(
//								new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
					}else {
						session_event = new EventFile();
						//new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.EVENT_DIRECTORY + valueToProcess).createNewFile();
					}
					
					session_match.setEvents(session_event.getEvents());
					session_match.setMatchFileTimeStamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(
							new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()).lastModified()));
					
				}
				
				File clockFile = new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CLOCK_JSON);
				
				
				if(session_match != null) {
					if(new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CLOCK_JSON).exists() && clockFile.canRead()) {
						
						session_clock = new ObjectMapper().readValue(clockFile, Clock.class);
						session_match.setClock(session_clock);
						
//						session_clock = (Clock) JAXBContext.newInstance(Clock.class).createUnmarshaller().unmarshal(
//								new File(FootballUtil.FOOTBALL_DIRECTORY + FootballUtil.CLOCK_XML));
//						session_match.setClock(session_clock);
						this_Football.updateScoreBug(session_selected_scenes, session_match, SwapMatch,footballService, print_writer);
					}
				}
			} catch (Exception e) {
	            // Handle specific exceptions
	            e.printStackTrace(); // Print stack trace for server-side debugging
	            
	            // Return error message as JSON string
	            JSONObject errorResponse = new JSONObject();
	            errorResponse.put("error", "Error processing request: " + e.getMessage());
	            return errorResponse.toString();
			}
			
			return JSONObject.fromObject(session_match).toString();
			
		default:
			
			this_Football.ProcessGraphicOption(whatToProcess, session_match, session_clock,SwapMatch, footballService, 
					print_writer, session_selected_scenes, valueToProcess);
			return JSONObject.fromObject(session_match).toString();
		}
	}
}