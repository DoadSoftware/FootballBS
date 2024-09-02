package com.football.broadcaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import com.football.containers.Scene;
import com.football.containers.ScoreBug;
import com.football.model.Clock;
import com.football.model.Event;
import com.football.model.Match;
import com.football.model.Player;
import com.football.model.Team;
import com.football.service.FootballService;
import com.football.util.FootballFunctions;
import com.football.util.FootballUtil;

public class Football extends Scene {

	public String session_selected_broadcaster = "FOOTBALL";

	public ScoreBug scorebug = new ScoreBug();
	public String which_graphics_onscreen = "";
	public boolean is_infobar = false,is_home_raider_in = false,is_away_raider_in = false;
	public int team_id=0;
	public long last_date = 0;
	public int Whichside = 2;
	public String logo_path = "C:/Images/Super_Cup/Logos/";
	
	public Football() {
		super();
	}

	public ScoreBug updateScoreBug(List<Scene> scenes, Match match, Match swapMatch,FootballService footballService, PrintWriter print_writer)throws InterruptedException, MalformedURLException, IOException {
		
		populateCommonData(print_writer,true, match,session_selected_broadcaster);
		
		if(which_graphics_onscreen.equalsIgnoreCase("SCOREBUG")) {
			scorebug = populateScoreBug(true, scorebug, print_writer, swapMatch,session_selected_broadcaster);
		}else if(which_graphics_onscreen.equalsIgnoreCase("SCORELINE")) {
			populateMatchStats(print_writer,true,footballService, match,session_selected_broadcaster);
		}else if(which_graphics_onscreen.equalsIgnoreCase("SUBS")) {
			populateMatchIdSubs(print_writer,true, match,session_selected_broadcaster);
		}
		return scorebug;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, Match match,Clock clock, Match swapMatch, FootballService footballService,PrintWriter print_writer, List<Scene> scenes, String valueToProcess)
			throws InterruptedException, NumberFormatException, MalformedURLException, IOException, JAXBException {
		switch (whatToProcess.toUpperCase()) {
		//ScoreBug
		case "POPULATE-MATCHID": case "POPULATE-SCORELINE": case "POPULATE-TOURNAMENT_LOGO": case "POPULATE-EXTRA_TIME":
		case "POPULATE-SCOREBUG-SUBS": case "POPULATE-MATCHSUBS": case "POPULATE-TIME_EXTRA":
			
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-MATCHID": case "POPULATE-SCORELINE": case "POPULATE-TOURNAMENT_LOGO": case "POPULATE-GOLDEN_RAID":
		case "POPULATE-MATCHSUBS":  case "POPULATE-TIME_EXTRA":
			scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
			break;
		}
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-SCOREBUG-SUBS":
			populateScorebugSubs(print_writer, scorebug, Integer.valueOf(valueToProcess.split(",")[0]), footballService.getAllPlayer(), match, 
					session_selected_broadcaster);
			break;
		case "POPULATE-TIME_EXTRA":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 4;");
			populateExtraTime(print_writer, false,scorebug,valueToProcess.split(",")[0],match,session_selected_broadcaster);
			populateCommonData(print_writer,false, match,session_selected_broadcaster);
			break;
		case "POPULATE-EXTRA_TIME":
			populateExtraTime(print_writer, false,scorebug,valueToProcess.split(",")[0],match,session_selected_broadcaster);
			break;
		case "POPULATE-MATCHID":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 1;");
			populateCommonData(print_writer,false, match, session_selected_broadcaster);
			break;
		case "POPULATE-MATCHSUBS":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 2;");
			populateCommonData(print_writer,true, match,session_selected_broadcaster);
			populateMatchIdSubs(print_writer, false, match, session_selected_broadcaster);
			break;
		case "POPULATE-SCORELINE":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 3;");
			populateCommonData(print_writer,true, match,session_selected_broadcaster);
			populateMatchStats(print_writer,false, footballService, match, session_selected_broadcaster);
			break;
		case "POPULATE-TOURNAMENT_LOGO":
			populateTournamentLogo(false, scorebug, print_writer, match, session_selected_broadcaster);
			break;
		}
		
		case "ANIMATE-IN-MATCHSUBS": case "ANIMATE-IN-TIME_EXTRA":
		case "ANIMATE-IN-MATCHID": case "CLEAR-ALL": case "ANIMATE-OUT-SCOREBUG": case "RESET-ALL-ANIMATION":
		case "ANIMATE-IN-SCORELINE": case "ANIMATE-OUT": case "ANIMATE-IN-TOURNAMENT_LOGO":
			
			switch (whatToProcess.toUpperCase()) {
			
			case "ANIMATE-IN-TIME_EXTRA":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "TIME_EXTRA";
				break;
			case "ANIMATE-IN-MATCHSUBS":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "SUBS";
				break;
			case "ANIMATE-IN-MATCHID":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "MATCHID";
				break;
			case "ANIMATE-IN-SCORELINE": 
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "SCORELINE";
				break;
			case "ANIMATE-IN-TOURNAMENT_LOGO":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "TOURNAMENT_LOGO";
				break;
			case "CLEAR-ALL":
				print_writer.println("LAYER1*EVEREST*SINGLE_SCENE CLEAR;");
				print_writer.println("LAYER2*EVEREST*SINGLE_SCENE CLEAR;");
				print_writer.println("LAYER3*EVEREST*SINGLE_SCENE CLEAR;");
				which_graphics_onscreen = "";
				is_infobar = false;
				break;
			case "ANIMATE-OUT-SCOREBUG":
				processAnimation(print_writer, "Out", "START", session_selected_broadcaster,1);
				is_infobar = false;
				which_graphics_onscreen="";
				break;
			case "RESET-ALL-ANIMATION":
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL_TIMER SET tAwayRaiderClock TIMER_OFFSET 30;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL_TIMER SET tHomeRaiderClock TIMER_OFFSET 30;");
				break;
			}
			break;
		}
		return null;
	}

	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer) throws IOException
	{
		switch(which_broadcaster.toUpperCase()) {
		case "FOOTBALL":
			switch(which_layer) {
			case 1:
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				break;
			case 2:
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				break;
			}
			break;
		}
	}
	
	public ScoreBug populateTournamentLogo(boolean isThisUpdating, ScoreBug scorebug, PrintWriter printWriter,
	        Match match, String selectedBroadcaster) throws IOException {
	    if (match == null) {
	        System.out.println("ERROR: ScoreBug -> Match is null");
	        return scorebug;
	    }
	    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 0;");
	   return scorebug;
	}
	
	public ScoreBug populateExtraTime(PrintWriter print_writer,boolean is_this_updating,ScoreBug scorebug, String time_value, Match match, String selectedbroadcaster) throws IOException {
		
		if(is_this_updating == false) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tExtraTime " + "+" + time_value + "'" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectExtra 1;");
		}
		return scorebug;
	}
	
	public ScoreBug populateScorebugSubs(PrintWriter print_writer,ScoreBug scorebug,int TeamId,List<Player> plyr, Match match, String selectedbroadcaster) throws InterruptedException, IOException
	{
		int l = 200;
		List<Event> evnt = new ArrayList<Event>();
		
		for(int i = 0; i<=match.getEvents().size()-1; i++) { 
			if(match.getEvents().get(i).getEventType().equalsIgnoreCase("replace")) {
				if(TeamId ==plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
					if(match.getHomeTeamId() == plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
						evnt.add(match.getEvents().get(i)); 
					}else if(match.getAwayTeamId() == plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
						evnt.add(match.getEvents().get(i)); 
					} 
				} 
			}
		}
		if(match.getHomeTeamId() == TeamId) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySubsSelect 0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeSubsName01 " + 
					plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + " " + 
					plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getTicker_name().toUpperCase() + ";");
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeSubsName02 " + 
					plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + " " + 
					plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getTicker_name().toUpperCase() + ";");
			
		}else if(match.getAwayTeamId() == TeamId) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSubsSelect 0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwaySubsName01 " + 
					plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + " " + 
					plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getTicker_name().toUpperCase() + ";");
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwaySubsName02 " + 
					plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + " " + 
					plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getTicker_name().toUpperCase() + ";");
		}
		
		return scorebug;
	}

	public void populateMatchStats(PrintWriter print_writer,boolean isThisUpdating,FootballService footballService, Match match, String session_selected_broadcaster) throws InterruptedException, IOException{
		int l = 4;
		//String Home_player="",Away_player="";
		String h1="",h2="",h3="",h4="",a1="",a2="",a3="",a4="";
		
		List<String> home_stats = new ArrayList<String>();
		List<String> away_stats = new ArrayList<String>();
		List<Integer> plyr_ids = new ArrayList<Integer>();
		boolean plyr_exist = false;
			String stats_txt = "",stats_txt_og = "";
		
		for(int i=0; i<=match.getMatchStats().size()-1; i++) {
			
			if((match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(FootballUtil.GOAL) 
					|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(FootballUtil.PENALTY))) {
				
				plyr_exist = false;
				for(Integer plyr_id : plyr_ids) {
					if(match.getMatchStats().get(i).getPlayerId() == plyr_id && 
							(match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(FootballUtil.GOAL) 
									|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(FootballUtil.OWN_GOAL)
									|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(FootballUtil.PENALTY))) {
						plyr_exist = true;
						break;
					}
				}

				if(plyr_exist == false) {
					plyr_ids.add(match.getMatchStats().get(i).getPlayerId());
					stats_txt = footballService.getPlayer(FootballUtil.PLAYER, 
						String.valueOf(match.getMatchStats().get(i).getPlayerId())).getTicker_name().toUpperCase()+ " " + 
						FootballFunctions.calExtraTimeGoal(match.getMatchStats().get(i).getMatchHalves(),match.getMatchStats().get(i).getTotalMatchSeconds()) + 
							FootballFunctions.goal_shortname(match.getMatchStats().get(i).getStats_type());
					
					for(int j=i+1; j<=match.getMatchStats().size()-1; j++) {
						if (match.getMatchStats().get(i).getPlayerId() == match.getMatchStats().get(j).getPlayerId()
							&& (match.getMatchStats().get(j).getStats_type().equalsIgnoreCase(FootballUtil.GOAL)
							|| match.getMatchStats().get(j).getStats_type().equalsIgnoreCase(FootballUtil.PENALTY))) {

							stats_txt = stats_txt + "," + 
								FootballFunctions.calExtraTimeGoal(match.getMatchStats().get(j).getMatchHalves(), match.getMatchStats().get(j).getTotalMatchSeconds()) 
									+ FootballFunctions.goal_shortname(match.getMatchStats().get(j).getStats_type());
						}
					}
					switch (FootballFunctions.getPlayerSquadType(match.getMatchStats().get(i).getPlayerId(),match.getMatchStats().get(i).getStats_type() ,match)) {
					case FootballUtil.HOME:
						home_stats.add(stats_txt);
						break;
					case FootballUtil.AWAY:
						away_stats.add(stats_txt);
						break;
					}
				}
			}else if(match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(FootballUtil.OWN_GOAL)) {
				stats_txt_og = footballService.getPlayer(FootballUtil.PLAYER, 
						String.valueOf(match.getMatchStats().get(i).getPlayerId())).getTicker_name().toUpperCase()+ " " + 
						FootballFunctions.calExtraTimeGoal(match.getMatchStats().get(i).getMatchHalves(),match.getMatchStats().get(i).getTotalMatchSeconds()) + 
							FootballFunctions.goal_shortname(match.getMatchStats().get(i).getStats_type());
					
					/*for(int j=i+1; j<=match.getMatchStats().size()-1; j++) {
						if (match.getMatchStats().get(i).getPlayerId() == match.getMatchStats().get(j).getPlayerId()
							&& (match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(FootballUtil.OWN_GOAL))) {

							stats_txt_og = stats_txt_og + "," + 
								FootballFunctions.calExtraTimeGoal(match.getMatchStats().get(j).getMatchHalves(), match.getMatchStats().get(j).getTotalMatchSeconds()) 
									+ FootballFunctions.goal_shortname(match.getMatchStats().get(j).getStats_type());
						}
					}*/
					switch (FootballFunctions.getPlayerSquadType(match.getMatchStats().get(i).getPlayerId(),match.getMatchStats().get(i).getStats_type() ,match)) {
					case FootballUtil.HOME:
						home_stats.add(stats_txt_og);
						break;
					case FootballUtil.AWAY:
						away_stats.add(stats_txt_og);
						break;
					}
			}
		}
		
		if(match.getHomeTeamScore() == 0 ) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSubsSelect 0;");
		}else if(home_stats.size() > 0 && home_stats.size() <= 2) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectLines 0;");
		}else if(home_stats.size() > 2 && home_stats.size() <= 4) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectLines 1;");
		}else if(home_stats.size() > 4 && home_stats.size() <= 6) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectLines 2;");
		}else {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectLines 3;");
		}
		
		if(match.getAwayTeamScore() == 0 ) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySubsSelect 0;");

		}else if(away_stats.size() > 0 && away_stats.size() <= 2) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectLines 0;");
		}else if(away_stats.size() > 2 && away_stats.size() <= 4) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectLines 1;");
		}else if(away_stats.size() > 4 && away_stats.size() <= 6) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectLines 2;");
		}else {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySubsSelect 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectLines 3;");
		}
		
		for(int i=0;i<=home_stats.size()-1;i++) {
			if(i < 2) { 
				h1 = h1 + home_stats.get(i); 
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeGoalerLine01 " + h1 + ";");
				TimeUnit.MILLISECONDS.sleep(l);
			}else if(i < 4) {
				h2 = h2 + home_stats.get(i);
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeGoalerLine02 " + h2 + ";");
				TimeUnit.MILLISECONDS.sleep(l);
			}else if(i < 6){
				h3 = h3 + home_stats.get(i);
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeGoalerLine03 " + h3 + ";");
				TimeUnit.MILLISECONDS.sleep(l);
			}else if(i < 8){
				h4 = h4 + home_stats.get(i);
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeGoalerLine04 " + h4 + ";");
				TimeUnit.MILLISECONDS.sleep(l);
			}
		}
		
		for(int i=0;i<=away_stats.size()-1;i++) {
			if(i < 2) { 
				a1 = a1 + away_stats.get(i); 
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayGoalerLine01 " + a1 + ";");
				TimeUnit.MILLISECONDS.sleep(l);
			}else if(i < 4) {
				a2 = a2 + away_stats.get(i);
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayGoalerLine02 " + a2 + ";");
				TimeUnit.MILLISECONDS.sleep(l);
			}else if(i < 6){
				a3 = a3 + away_stats.get(i);
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayGoalerLine03 " + a3 + ";");
				TimeUnit.MILLISECONDS.sleep(l);
			}else if(i < 8){
				a4 = a4 + away_stats.get(i);
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayGoalerLine04 " + a4 + ";");
				TimeUnit.MILLISECONDS.sleep(l);
			}
		}
		
		if(isThisUpdating == false) {
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In STOP;");
		    print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out STOP;");
		    print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 88.0;");
		    print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		    print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
		    print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
		}
	}
	public ScoreBug populateScoreBug(boolean isThisUpdating, ScoreBug scorebug, PrintWriter printWriter,
	        Match match, String selectedBroadcaster) throws IOException {
	    if (match == null) {
	        System.out.println("ERROR: ScoreBug -> Match is null");
	        return scorebug;
	    }

	    if (!isThisUpdating) {
	        updateStaticMatchInfo(printWriter, match);
	    }

	    updateScore(printWriter, match);
	    
	    updateMatchClock(printWriter, match);

	    return scorebug;
	}

	private void updateStaticMatchInfo(PrintWriter printWriter, Match match) {
//	    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 1;");
//	    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getMatchIdent() + ";");
//	    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
//	            match.getHomeTeam().getTeamName4() + ".png" + ";");
//	    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + 
//	            match.getAwayTeam().getTeamName4() + ".png" + ";");
	}

	private void updateMatchClock(PrintWriter printWriter, Match match) {
	    if (match.getClock() != null && match.getClock().getMatchHalves() != null) {
	        String matchHalf = match.getClock().getMatchHalves();
	        String subHeader;
	        int active_value = 1;
	        
	        switch (matchHalf) {
		        case "first":
	                subHeader = "FIRST HALF";
	                active_value = 1;
	                break;
	            case "second":
	                subHeader = "SECOND HALF";
	                active_value = 1;
	                break;
	            case "extra1":
	                subHeader = "EXTRA TIME 1";
	                active_value = 1;
	                break;
	            case "extra2":
	                subHeader = "EXTRA TIME 2";
	                active_value = 1;
	                break;
	            case "half":
	                subHeader = match.getClock().getMatchHalves().toUpperCase() + " TIME";
	                active_value = 1;
	                break;
	            case "full":
	                subHeader = match.getClock().getMatchHalves().toUpperCase() + " TIME";
	                active_value = 1;
	                break;
	            default:
	                subHeader = "";
	                active_value = 0;
	                break;
	        }

	        printWriter.println("LAYER1*EVEREST*TREEVIEW*Main$Select$Data$Clock*CONTAINER SET ACTIVE " + active_value + ";");
	        printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader02 " + subHeader + ";");
	    }
	}


	private void updateScore(PrintWriter printWriter, Match match) {
	    String score = match.getHomeTeamScore() + "-" + match.getAwayTeamScore();
	    
	    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + match.getHomeTeamScore() + ";");
	    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + match.getAwayTeamScore() + ";");
	}

	
	
	public void populateMatchIdSubs(PrintWriter printWriter, boolean isThisUpdating,Match match, String selectedbroadcaster) throws IOException, InterruptedException {
		if (match == null) {
			System.out.println("ERROR: ScoreBug -> Match is null");
		} else {
			
		    if (match.getClock() != null && match.getClock().getMatchHalves() != null) {
		        String matchHalf = match.getClock().getMatchHalves();
		        String subHeader;
		        
		        switch (matchHalf) {
			        case "first":
		                subHeader = "FIRST HALF";
		                break;
		            case "second":
		                subHeader = "SECOND HALF";
		                break;
		            case "extra1":
		                subHeader = "EXTRA TIME 1";
		                break;
		            case "extra2":
		                subHeader = "EXTRA TIME 2";
		                break;
		            case "half":
		                subHeader = match.getClock().getMatchHalves().toUpperCase() + " TIME";
		                break;
		            case "full":
		                subHeader = match.getClock().getMatchHalves().toUpperCase() + " TIME";
		                break;
		            default:
		                subHeader = "";
		                break;
		        }

		        printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader02 " + subHeader + ";");
		    }
		    
		    if(isThisUpdating == false) {
		    	
		    	printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectExtra 0;");
		    	
		    	printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSubsSelect 0;");
		    	printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySubsSelect 0;");
		    	
				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tExtraTime 0;");
				
				printWriter.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
			    printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*In STOP;");
			    printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out STOP;");
			    printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 88.0;");
			    printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			    printWriter.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
			    printWriter.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
				TimeUnit.SECONDS.sleep(1);
				printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
				printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				printWriter.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
			}
		}
	}
	
	public void populateCommonData(PrintWriter printWriter,boolean isThisUpdating, Match match, String selectedbroadcaster) throws IOException, InterruptedException {
		if (match == null) {
			System.out.println("ERROR: ScoreBug -> Match is null");
		} else {
			
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + 
					match.getMatchIdent().toUpperCase() + ";");
			
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
		            match.getHomeTeam().getTeamName4() + ".png" + ";");
		    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + 
		            match.getAwayTeam().getTeamName4() + ".png" + ";");
		    
		    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
		            match.getHomeTeam().getTeamName1() + ";");
		    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
		            match.getAwayTeam().getTeamName1() + ";");
		    
		    
		    if(match.getHomeTeamScore() == 0 && match.getAwayTeamScore() == 0) {
		    	printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vScoreVS 0;");
		    }else {
		    	printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vScoreVS 1;");
		    	printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + 
		    			match.getHomeTeamScore() + "-" + match.getAwayTeamScore() + ";");
		    }
		}
		
		if(isThisUpdating == false) {
			printWriter.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		    printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*In STOP;");
		    printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out STOP;");
		    printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 88.0;");
		    printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		    printWriter.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
		    printWriter.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
			TimeUnit.SECONDS.sleep(1);
			printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
			printWriter.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			printWriter.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
		}
	}
}
