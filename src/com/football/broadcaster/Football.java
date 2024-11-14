package com.football.broadcaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Pattern.Flag;
import javax.xml.bind.JAXBException;

import com.football.containers.Scene;
import com.football.containers.ScoreBug;
import com.football.model.Clock;
import com.football.model.Event;
import com.football.model.Formation;
import com.football.model.Match;
import com.football.model.Officials;
import com.football.model.Player;
import com.football.model.Team;
import com.football.model.VariousText;
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
	public static List<String> penalties;
	public static List<String> penaltiesremove;
	
	public Football() {
		super();
	}

	public ScoreBug updateScoreBug(List<Scene> scenes, Match match, Match swapMatch,FootballService footballService, PrintWriter print_writer)throws InterruptedException, MalformedURLException, IOException {
		
		if(which_graphics_onscreen.equalsIgnoreCase("SCORELINE")) {
			populateCommonData(print_writer,true, match,session_selected_broadcaster);
			populateMatchStats(print_writer,true,footballService, match,session_selected_broadcaster);
		}else if(which_graphics_onscreen.equalsIgnoreCase("SUBS")) {
			populateCommonData(print_writer,true, match,session_selected_broadcaster);
			populateMatchIdSubs(print_writer,true, match,session_selected_broadcaster);
		}else if(which_graphics_onscreen.equalsIgnoreCase("MATCHSTATS")) {
			populateCommonData(print_writer,true, match,session_selected_broadcaster);
		}
		return scorebug;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, Match match,Clock clock, Match swapMatch, FootballService footballService,PrintWriter print_writer, List<Scene> scenes, String valueToProcess)
			throws InterruptedException, NumberFormatException, MalformedURLException, IOException, JAXBException {
		
		if (which_graphics_onscreen == "PENALTY")
		{
			int iHomeCont = 0, iAwayCont = 0;
			penalties.add(valueToProcess.split(",")[0]);
			
			for(String pen : penalties)
			{	
				if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 2;");
					
				}else if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 1;");
				}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 2;");
				}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 1;");
				}else if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 0;");
					
					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
				}else if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 0;");

					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
				}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 0;");

					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 0;");

					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}
			}
			if(match.getHomePenaltiesHits() == 0 && match.getAwayPenaltiesHits() == 0 && 
					match.getHomePenaltiesMisses() == 0 && match.getAwayPenaltiesMisses() == 0) {
				penalties = new ArrayList<String>();
				penaltiesremove = new ArrayList<String>();
			}
		} 
		else {
			for(int p=1;p<=5;p++) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + p + " 0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + p + " 0;");
			}
			
			if(penalties == null) {
				penalties = new ArrayList<String>();
				penaltiesremove = new ArrayList<String>();
			}
			if(match.getHomePenaltiesHits() == 0 && match.getAwayPenaltiesHits() == 0 && 
					match.getHomePenaltiesMisses() == 0 && match.getAwayPenaltiesMisses() == 0) {
				penalties = new ArrayList<String>();
				penaltiesremove = new ArrayList<String>();
			}
			int iHomeCont = 0, iAwayCont = 0;
			penalties.add(valueToProcess.split(",")[0]);
			if((match.getHomePenaltiesHits()+match.getHomePenaltiesMisses()) != 0 && (match.getAwayPenaltiesHits()+match.getAwayPenaltiesHits()) != 0) {
				if(((match.getHomePenaltiesHits() + match.getHomePenaltiesMisses())%5) == 0 && 
						((match.getAwayPenaltiesHits() + match.getAwayPenaltiesMisses())%5) == 0) {
					if(match.getHomePenaltiesHits() == match.getAwayPenaltiesHits()) {
						penalties = new ArrayList<String>();
//						for(int p=1;p<=5;p++) {
//							print_writer.get(0).println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + p + "$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
//							print_writer.get(0).println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + p + "$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
//						}
					}
				}
			}
			
			//print_writer.get(0).println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + match.getHomePenaltiesHits() + "-" + match.getAwayPenaltiesHits() + ";");

			for(String pen : penalties)
			{
				//System.out.println("ELSE LOOP - " + iHomeCont);
				if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 2;");
				}else if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 1;");
				}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 2;");
				}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 1;");
				}else if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 0;");

					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
					
				}else if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 0;");

					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
				}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 0;");
			
					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 0;");
					
					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}
			}
		}
		
		switch (whatToProcess.toUpperCase()) {
		//ScoreBug
		case "POPULATE-MATCHID": case "POPULATE-SCORELINE": case "POPULATE-TOURNAMENT_LOGO": case "POPULATE-EXTRA_TIME":
		case "POPULATE-SCOREBUG-SUBS": case "POPULATE-MATCHSUBS": case "POPULATE-TIME_EXTRA": case "POPULATE-MATCHSTATS":
		case "POPULATE-TEAMLIST": case "POPULATE-L3-NAMESUPER-CARD": case "POPULATE-OFFICIALS": case "POPULATE-GOAL":
		case "POPULATE-ATTENDENCE": case "POPULATE-AIFF": case "POPULATE-ADDITIONAL": case "POPULATE-FREE":
		case "POPULATE-WELCOME": case "POPULATE-SECURITY": case "POPULATE-NOTICE":	case "POPULATE-PENALTY":
		case "POPULATE-CHANGE_PENALTY":	
			
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-MATCHID": case "POPULATE-SCORELINE": case "POPULATE-TOURNAMENT_LOGO": case "POPULATE-L3-NAMESUPER-CARD":
		case "POPULATE-MATCHSUBS":  case "POPULATE-TIME_EXTRA": case "POPULATE-MATCHSTATS": case "POPULATE-TEAMLIST":
		case "POPULATE-SCOREBUG-SUBS": case "POPULATE-OFFICIALS": case "POPULATE-GOAL": case "POPULATE-ATTENDENCE":
		case "POPULATE-AIFF": case "POPULATE-ADDITIONAL": case "POPULATE-FREE": case "POPULATE-PENALTY":
		case "POPULATE-WELCOME": case "POPULATE-SECURITY": case "POPULATE-NOTICE":	
			scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
			break;
		}
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-PENALTY":
			populateLtPenalty(print_writer,false, footballService,match,clock, session_selected_broadcaster);
			break;
		case "POPULATE-CHANGE_PENALTY":
			populateLtPenaltyChange(print_writer, match,session_selected_broadcaster);
			break;
		case "POPULATE-WELCOME":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 11;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectMessage 0;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText01 WELCOME MESSAGE;");
			
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
			break;
		case "POPULATE-SECURITY":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 11;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectMessage 2;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText01 SECURITY ANNOUNCEMENT;");
			
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
			break;
		case "POPULATE-NOTICE":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 11;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectMessage 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText01 IMPORTANCE NOTICE OF ENTERING;");
			
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
			break;	
		case "POPULATE-ATTENDENCE":
			populateAttendence(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
			break;
		case "POPULATE-AIFF":
			populateAiff(print_writer);
			break;
		case "POPULATE-ADDITIONAL":
			populateAdditional(print_writer,valueToProcess.split(",")[0], match, session_selected_broadcaster);
			break;
		case "POPULATE-FREE":
			populateFree(print_writer,valueToProcess.split(",")[0],valueToProcess.split(",")[1], match, session_selected_broadcaster);
			break;	
		case "POPULATE-GOAL":
			populateGoal(print_writer,Integer.valueOf(valueToProcess.split(",")[0]), Integer.valueOf(valueToProcess.split(",")[1]), match,footballService, session_selected_broadcaster);
			break;
		case "POPULATE-OFFICIALS":
			populateOfficials(print_writer,footballService.getOfficials(),match, session_selected_broadcaster);
			break;
		case "POPULATE-L3-NAMESUPER-CARD":
			populateNameSuperCard(print_writer, Integer.valueOf(valueToProcess.split(",")[0]), 
					valueToProcess.split(",")[1], Integer.valueOf(valueToProcess.split(",")[2]), match, session_selected_broadcaster);
			break;
		case "POPULATE-TEAMLIST":
			populatePlayingXI(print_writer, Integer.valueOf(valueToProcess.split(",")[0]),
					footballService.getTeams(),footballService.getVariousTexts(),match, session_selected_broadcaster);
			break;
		case "POPULATE-SCOREBUG-SUBS":
			populateScorebugSubs(print_writer, scorebug,valueToProcess.split(",")[0] ,Integer.valueOf(valueToProcess.split(",")[1]), footballService.getAllPlayer(), match, 
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
		case "POPULATE-MATCHSTATS":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 1;");
			populateMatchData(print_writer,false, match, session_selected_broadcaster);
			populateCommonData(print_writer,false, match, session_selected_broadcaster);
			break;
		case "POPULATE-MATCHID":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 1;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vScoreVS 0;");
//			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + 
//					match.getMatchIdent().toUpperCase() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader ;");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
		            match.getHomeTeam().getTeamName4() + ".png" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + 
		            match.getAwayTeam().getTeamName4() + ".png" + ";");
		    
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
		            match.getHomeTeam().getTeamName1() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
		            match.getAwayTeam().getTeamName1() + ";");
			
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
			//populateCommonData(print_writer,false, match, session_selected_broadcaster);
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
		
		case "ANIMATE-IN-MATCHSUBS": case "ANIMATE-IN-TIME_EXTRA": case "ANIMATE-IN-MATCHSTATS":
		case "ANIMATE-IN-MATCHID": case "CLEAR-ALL": case "ANIMATE-OUT-SCOREBUG": case "RESET-ALL-ANIMATION":
		case "ANIMATE-IN-SCORELINE": case "ANIMATE-OUT": case "ANIMATE-IN-TOURNAMENT_LOGO":
		case "ANIMATE-IN-NAMESUPER-CARD": case "ANIMATE-IN-TEAMLIST": case "ANIMATE-IN-SCOREBUG-SUBS":
		case "ANIMATE-IN-OFFICIALS": case "ANIMATE-IN-GOAL": case "ANIMATE-IN-FREE": case "ANIMATE-IN-ADDITIONAL":
		case "ANIMATE-IN-AIFF": case "ANIMATE-IN-ATTENDENCE": case "ANIMATE-IN-WELCOME": case "ANIMATE-IN-SECURITY": 
		case "ANIMATE-IN-NOTICE": case "ANIMATE-IN-PENALTY":
			
			
			switch (whatToProcess.toUpperCase()) {
			case "ANIMATE-IN-PENALTY":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "PENALTY";
				break;
			case "ANIMATE-IN-WELCOME": case "ANIMATE-IN-SECURITY": case "ANIMATE-IN-NOTICE":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "ADD";
				break;
			case "ANIMATE-IN-ADDITIONAL":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "ADDITIONAL";
				break;
			case "ANIMATE-IN-AIFF":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "AIFF";
				break;
			case "ANIMATE-IN-ATTENDENCE":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "ATTENDENCE";
				break;	
			case "ANIMATE-IN-FREE":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "FREE";
				break;
			case "ANIMATE-IN-GOAL":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "GOAL";
				break;
			case "ANIMATE-IN-OFFICIALS":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "OFFICIALS";
				break;
			case "ANIMATE-IN-SCOREBUG-SUBS":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "SCOREBUG-SUBS";
				break;
			case "ANIMATE-IN-TEAMLIST":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "TEAMLIST";
				break;
			case "ANIMATE-IN-NAMESUPER-CARD":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "NAMESUPER-CARD";
				break;
			case "ANIMATE-IN-MATCHSTATS":
				processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
				is_infobar = true;
				which_graphics_onscreen = "MATCHSTATS";
				break;
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
			case "ANIMATE-OUT":
				switch (which_graphics_onscreen) {
				case "ADD":
					processAnimation(print_writer, "Out", "START", session_selected_broadcaster,1);
					which_graphics_onscreen = "";
					break;

				default:
					break;
				}
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
	
	public ScoreBug populateScorebugSubs(PrintWriter print_writer,ScoreBug scorebug,String Type,int TeamId,List<Player> plyr, Match match, String selectedbroadcaster) throws InterruptedException, IOException
	{
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 9;");
		
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader SUBSTITUTIONS;");
		
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
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + 
		            match.getHomeTeam().getTeamName4() + ".png" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + match.getHomeTeam().getTeamName1() + ";");
		}else if(match.getAwayTeamId() == TeamId) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + 
		            match.getAwayTeam().getTeamName4() + ".png" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + match.getAwayTeam().getTeamName1() + ";");
		}
		
		
		switch(Type.toUpperCase())
		{
		case "SINGLE":
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubs 0;");
			
			if(match.getHomeTeamId() == TeamId) {
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 ;");
				}
				
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 ;");
				}
			}else if(match.getAwayTeamId() == TeamId) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 ;");
				}
				
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 ;");
				}
			}
			
			break;
		case "DOUBLE":
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubs 1;");
			
			if(match.getHomeTeamId() == TeamId) {
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 ;");
				}
				
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber02 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName02 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName02 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName02 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber02 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName02 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName02 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName02 ;");
				}
				
			}else if(match.getAwayTeamId() == TeamId) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 ;");
				}
				
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber02 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName02 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName02 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName02 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber02 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName02 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName02 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName02 ;");
				}
			}
			
			break;
			
		case "TRIPLE":
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubs 2;");
			
			if(match.getHomeTeamId() == TeamId) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber02 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName02 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName02 " + 
							plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName02 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber02 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName02 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName02 " + 
							plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName02 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber03 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName03 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName03 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName03 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber03 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName03 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName03 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName03 ;");
				}
				
				
			}else if(match.getAwayTeamId() == TeamId) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName01 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber01 " + 
						plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName01 " + 
						plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName01 " + 
							plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber02 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName02 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName02 " + 
							plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName02 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber02 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName02 " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName02 " + 
							plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName02 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutNumber03 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutFirstName03 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName03 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOutLastName03 ;");
				}
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInNumber03 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInFirstName03 " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getFirstname().toUpperCase() + ";");
				
				if(plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname() != null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName03 " + 
							plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getSurname().toUpperCase() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tInLastName03 ;");
				}
			}
			
			break;
		}
		
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
		
		return scorebug;
	}

	public void populateLtPenalty(PrintWriter print_writer,boolean is_this_updating,FootballService footballService,Match match,Clock clock, String session_selected_broadcaster) 
			throws InterruptedException, IOException{
		
		int l=200;
		int iHomeCont = 0, iAwayCont = 0;
		
		if(is_this_updating == false) {
			for(int p=1;p<=5;p++) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + p + " 0;");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + p + " 0;");
			}
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 12;");
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
		            match.getHomeTeam().getTeamName4() + ".png" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + 
		            match.getAwayTeam().getTeamName4() + ".png" + ";");
		    
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
		            match.getHomeTeam().getTeamName1() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
		            match.getAwayTeam().getTeamName1() + ";");
			
//			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getMatchIdent() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader ;");
		}
		
		TimeUnit.MILLISECONDS.sleep(l);
		
//		for(int p=1;p<=5;p++) {
//			print_writer.get(0).println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + p + "$SelectPenaltyType$txt_PenaltyNumber*GEOM*TEXT SET " + 
//					p + "\0");
//			print_writer.get(0).println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + p + "$SelectPenaltyType$txt_PenaltyNumber*GEOM*TEXT SET " + 
//					p + "\0");
//		}
		
		for(String pen : penalties)
		{
			if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
				iHomeCont = iHomeCont + 1;
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 2;");
				
			}else if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
				iHomeCont = iHomeCont + 1;
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 1;");
				
			}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES$" + "_" + FootballUtil.HIT)) {
				iAwayCont = iAwayCont + 1;
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 2;");
				
			}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.INCREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
				iAwayCont = iAwayCont + 1;
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 1;");
			}
			
			
			if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 0;");
				if(iHomeCont > 0) {
					iHomeCont = iHomeCont - 1;
				}
			}else if(pen.toUpperCase().contains(FootballUtil.HOME + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + iHomeCont + " 0;");
				if(iHomeCont > 0) {
					iHomeCont = iHomeCont - 1;
				}
			}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.HIT)) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 0;");
				if(iAwayCont > 0) {
					iAwayCont = iAwayCont - 1;
				}
			}else if(pen.toUpperCase().contains(FootballUtil.AWAY + "_" + FootballUtil.DECREMENT + "_" + "PENALTIES" + "_" + FootballUtil.MISS)) {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + iAwayCont + " 0;");
				if(iAwayCont > 0) {
					iAwayCont = iAwayCont - 1;
				}
			}
		}
	}
	public void populateLtPenaltyChange(PrintWriter print_writer,Match match, String session_selected_broadcaster) throws InterruptedException, IOException{
		
		int iHomeCont = 0, iAwayCont = 0;
		int HomeTotal = 0,AwayTotal=0;
		
		iHomeCont = (match.getHomePenaltiesHits() + match.getHomePenaltiesMisses());
		iAwayCont = (match.getAwayPenaltiesHits() + match.getAwayPenaltiesMisses());
		
		HomeTotal = iHomeCont + 5;
		AwayTotal = iAwayCont + 5;
		
		if(((match.getHomePenaltiesHits()+match.getHomePenaltiesMisses())%5) == 0 && ((match.getAwayPenaltiesHits()+match.getAwayPenaltiesMisses())%5) == 0) {
			if(match.getHomePenaltiesHits() == match.getAwayPenaltiesHits()) {
				penalties = new ArrayList<String>();
					for(int p=1;p<=5;p++) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomePenalty0" + p + " 0;");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwayPenalty0" + p + " 0;");
					}
			}
		}
		
//		for(int h=iHomeCont+1;h<=HomeTotal;h++) {
//			print_writer.get(0).println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + (h-iHomeCont) + "$SelectPenaltyType$txt_PenaltyNumber*GEOM*TEXT SET " + 
//					h + "\0");
//		}
//		
//		for(int a=iAwayCont+1;a<=AwayTotal;a++) {
//			print_writer.get(0).println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + (a-iAwayCont) + "$SelectPenaltyType$txt_PenaltyNumber*GEOM*TEXT SET " + 
//					a + "\0");
//		}
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
			
//			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getMatchIdent() + ";");
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader ;");
			
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
		    
		    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vScoreVS 1;");
	    	printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + 
	    			match.getHomeTeamScore() + "-" + match.getAwayTeamScore() + ";");
	    	
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
	
	public void populateMatchData(PrintWriter printWriter,boolean isThisUpdating, Match match, String selectedbroadcaster) throws IOException, InterruptedException {
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

		        printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + subHeader + ";");
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
	
	public void populateCommonData(PrintWriter printWriter,boolean isThisUpdating, Match match, String selectedbroadcaster) throws IOException, InterruptedException {
		if (match == null) {
			System.out.println("ERROR: ScoreBug -> Match is null");
		} else {
			
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
		            match.getHomeTeam().getTeamName4() + ".png" + ";");
		    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgAwayTeamLogo " + logo_path + 
		            match.getAwayTeam().getTeamName4() + ".png" + ";");
		    
		    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
		            match.getHomeTeam().getTeamName1() + ";");
		    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
		            match.getAwayTeam().getTeamName1() + ";");
		    
		    printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vScoreVS 1;");
	    	printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + 
	    			match.getHomeTeamScore() + "-" + match.getAwayTeamScore() + ";"); 
		   
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
	
	public void populateNameSuperCard(PrintWriter printWriter, int TeamId, String cardType, int playerId, Match match, String selectedbroadcaster) throws InterruptedException, IOException
	{
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 7;");
		
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader PLAYER CARD;");
		
		if(TeamId == match.getHomeTeamId()) {
			
			for(Player hs : match.getHomeSquad()) {
				if(playerId == hs.getPlayerId()) {
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNumber " + hs.getJersey_number() + ";");
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + hs.getFirstname().toUpperCase() + ";");
					
					if(hs.getSurname() != null) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + hs.getSurname().toUpperCase() + ";");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName ;");
					}
					
				}
			}
			for(Player hsub : match.getHomeSubstitutes()) {
				if(playerId == hsub.getPlayerId()) {
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNumber " + hsub.getJersey_number() + ";");
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + hsub.getFirstname().toUpperCase() + ";");
					if(hsub.getSurname() != null) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + hsub.getSurname().toUpperCase() + ";");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName ;");
					}
				}
			}
		}
		else {
			
			for(Player as : match.getAwaySquad()) {
				if(playerId == as.getPlayerId()) {
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNumber " + as.getJersey_number() + ";");
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + as.getFirstname().toUpperCase() + ";");
					if(as.getSurname() != null) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + as.getSurname().toUpperCase() + ";");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName ;");
					}
				}
			}
			for(Player asub : match.getAwaySubstitutes()) {
				if(playerId == asub.getPlayerId()) {
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNumber " + asub.getJersey_number() + ";");
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + asub.getFirstname().toUpperCase() + ";");
					if(asub.getSurname() != null) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + asub.getSurname().toUpperCase() + ";");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName ;");
					}
				}
			}
		}
		
		switch(cardType.toUpperCase())
		{
		case FootballUtil.YELLOW:
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCards 0;");
			break;
		case FootballUtil.RED:
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCards 2;");
			break;
		case "YELLOW_RED":
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCards 1;");
			break;
		}
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
	
	public void populateOfficials(PrintWriter printWriter,List<Officials> officials,Match match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 8;");

		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader MATCH OFFICIALS;");
		
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tName01 " + officials.get(0).getReferee() + ";");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tName02 " + officials.get(0).getAssistantReferee1() + ";");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tName03 " + officials.get(0).getAssistantReferee2() + ";");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tName04 " + officials.get(0).getFourthOfficial() + ";");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tName05 " + officials.get(0).getMatchCommissioner() + ";");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tName06 " + officials.get(0).getRefereeAssessor() + ";");
		
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDesig01 REFEREE;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDesig02 ASSISTANT REFEREE 1;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDesig03 ASSISTANT REFEREE 2;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDesig04 FOURTH OFFICIAL;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDesig05 MATCH COMMISSIONER;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDesig06 REFEREE ASSESSOR;");
		
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
	
	public void populateAttendence(PrintWriter printWriter,String data, Match match, String selectedBroadcaster) throws InterruptedException {
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 10;");
		
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText01 ATTENDANCE;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText02 " + data + ";");
		
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
	
	public void populateAiff(PrintWriter printWriter) throws InterruptedException {
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 10;");
		
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText01 ;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText02 AIFF MEETING;");
		
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
	
	public void populateAdditional(PrintWriter printWriter,String data, Match match, String selectedBroadcaster) throws InterruptedException {
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 10;");
		
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText01 ADDITIONAL TIME;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText02 " + data + " MINUTES;");
		
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
	
	public void populateFree(PrintWriter printWriter,String data1,String data2, Match match, String selectedBroadcaster) throws InterruptedException {
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 10;");
		
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText01 " + data1 + ";");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFreeText02 " + data2 + ";");
		
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
	
	public void populateGoal(PrintWriter printWriter,int teamId, int playerId, Match match, FootballService footService, String selectedBroadcaster) throws InterruptedException {
		Player player = footService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == playerId).findAny().orElse(null);
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 6;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader GOAL SCORER;");
		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNumber " + player.getJersey_number() + ";");
		if(teamId == match.getHomeTeamId()) {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + match.getHomeTeam().getTeamName1() + ";");
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
		            match.getHomeTeam().getTeamName4() + ".png" + ";");
		}else {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + match.getAwayTeam().getTeamName1() + ";");
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgHomeTeamLogo " + logo_path + 
		            match.getAwayTeam().getTeamName4() + ".png" + ";");
		}
		if(player.getSurname() != null) {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + player.getFirstname() + ";");
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + player.getSurname() + ";");
		}else {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFirstName " + player.getFirstname() + ";");
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastName " + "" + ";");
		}
		
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
	
	public void populatePlayingXI(PrintWriter printWriter, int TeamId, List<Team> team ,List<VariousText> var,
			Match match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{	
			int row_id = 0,row_id_sub = 0,l=100;
			
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectLogo_Data 5;");
			
//			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader " + match.getMatchIdent() + ";");
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubHeader ;");
			
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayingHead STARTING XI;");
			
			for(int i=1;i<=7;i++) {
				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main$Select$TeamList$Subs$1-7$" + i + "*CONTAINER SET ACTIVE 0;");
			}
			
			for(int i=8;i<=14;i++) {
				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main$Select$TeamList$Subs$8-14$" + i + "*CONTAINER SET ACTIVE 0;");
			}
			
			if(TeamId == match.getHomeTeamId()) {
				
				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + 
						team.get(TeamId-1).getTeamName1().toUpperCase() + ";");
				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + 
						logo_path + team.get(TeamId-1).getTeamName4() + ".png" + ";");
				
				if(team.get(TeamId-1).getTeamCoach() == null) {
					if(team.get(TeamId-1).getTeamAssistantCoach() == null) {
						
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.YES)) {
								printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead " + vartext.getVariousText() + ";");
								
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.NO)) {
								printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead ;");
								
							}
						}
					}else {

						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.YES)) {
								printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead " + 
										vartext.getVariousText() + ";");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.NO)) {
								printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead " + "ASST. COACH" + ";");
							}
						}
						
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachName " + 
								team.get(TeamId-1).getTeamAssistantCoach() + ";");
					}
					
				}else {
					for(VariousText vartext : var) {
						if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.YES)) {
							printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead " + vartext.getVariousText() + ";");
						}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.NO)) {
							printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead " + "COACH" + ";");
						}
					}
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachName " + 
							team.get(TeamId-1).getTeamCoach() + ";");
				}
				
				for(Player hs : match.getHomeSquad()) {
					row_id = row_id + 1;
					
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNumber0" + row_id + " " + hs.getJersey_number() + ";");
					
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tName0" + row_id + " " + hs.getTicker_name() + ";");
					
					
					if(hs.getCaptainGoalKeeper().equalsIgnoreCase(FootballUtil.CAPTAIN)) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubRole0" + row_id + " 1;");
					}else if(hs.getCaptainGoalKeeper().equalsIgnoreCase(FootballUtil.GOAL_KEEPER)) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubRole0" + row_id + " 2;");
					}else if(hs.getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubRole0" + row_id + " 3;");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubRole0" + row_id + " 0;");
					}
				}
				
				for(Player hsub : match.getHomeSubstitutes()) {
					row_id_sub = row_id_sub + 1;
					
					
					if(match.getHomeSubstitutes().size() > 7 && row_id_sub > 7) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main$Select$TeamList$Subs$8-14$" + row_id_sub + "*CONTAINER SET ACTIVE 1;");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main$Select$TeamList$Subs$1-7$" + row_id_sub + "*CONTAINER SET ACTIVE 1;");
					}
//					print_writer.get(0).println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayerIn" + row_id + " SHOW 0.0 \0");
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubNumber0" + row_id_sub + " " + hsub.getJersey_number() + ";");
					
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubsName0" + row_id_sub + " " + hsub.getTicker_name() + ";");
					
					if(hsub.getCaptainGoalKeeper().equalsIgnoreCase(FootballUtil.GOAL_KEEPER)) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectRole0" + row_id_sub + " 2;");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectRole0" + row_id_sub + " 0;");
					}
				}
				
			}else if(TeamId == match.getAwayTeamId()) {
				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName " + 
						team.get(TeamId-1).getTeamName1().toUpperCase() + ";");
				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + 
						logo_path + team.get(match.getAwayTeamId() -1).getTeamName4() + ".png" + ";");
				
				if(team.get(TeamId-1).getTeamCoach() == null) {
					if(team.get(TeamId-1).getTeamAssistantCoach() == null) {
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.YES)) {
								printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead " + vartext.getVariousText() + ";");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.NO)) {
								printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead ;");
							}
						}
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachName ;");
					}else {
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.YES)) {
								printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead " + vartext.getVariousText() + ";");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.NO)) {
								printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead ASST. COACH;");
							}
						}
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachName " + team.get(TeamId-1).getTeamAssistantCoach() + ";");
						
					}
					
				}else {
					for(VariousText vartext : var) {
						if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.YES)) {
							printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead " + vartext.getVariousText() + ";");
						}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(FootballUtil.NO)) {
							printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachHead COACH;");
						}
					}
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tCoachName " + team.get(TeamId-1).getTeamCoach() + ";");
					
				}
				row_id = 0;
				for(Player as : match.getAwaySquad()) {
					row_id = row_id + 1;
					
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNumber0" + row_id + " " + as.getJersey_number() + ";");
					
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tName0" + row_id + " " + as.getTicker_name() + ";");
					
					
					if(as.getCaptainGoalKeeper().equalsIgnoreCase(FootballUtil.CAPTAIN)) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubRole0" + row_id + " 1;");
					}else if(as.getCaptainGoalKeeper().equalsIgnoreCase(FootballUtil.GOAL_KEEPER)) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubRole0" + row_id + " 2;");
					}else if(as.getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubRole0" + row_id + " 3;");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectSubRole0" + row_id + " 0;");
					}
				}
				
				row_id_sub = 0;
				for(Player asub : match.getAwaySubstitutes()) {
					row_id_sub = row_id_sub + 1;
					
					
					if(match.getAwaySubstitutes().size() > 7 && row_id_sub > 7) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main$Select$TeamList$Subs$8-14$" + row_id_sub + "*CONTAINER SET ACTIVE 1;");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main$Select$TeamList$Subs$1-7$" + row_id_sub + "*CONTAINER SET ACTIVE 1;");
					}
					
//					print_writer.get(0).println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayerIn" + row_id + " SHOW 0.0 \0");
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubNumber0" + row_id_sub + " " + asub.getJersey_number() + ";");
					
					printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSubsName0" + row_id_sub + " " + asub.getTicker_name() + ";");
					
					
					if(asub.getCaptainGoalKeeper().equalsIgnoreCase(FootballUtil.GOAL_KEEPER)) {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectRole0" + row_id_sub + " 2;");
					}else {
						printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectRole0" + row_id_sub + " 0;");
					}
				}
			}
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
