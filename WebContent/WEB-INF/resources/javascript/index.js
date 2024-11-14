var match_data;
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;	
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
}
function millisToMinutesAndSeconds(millis) {
  var m = Math.floor(millis / 60000);
  var s = ((millis % 60000) / 1000).toFixed(0);
  return (m < 10 ? '0' + m : m) + ":" + (s < 10 ? '0' + s : s);
}
function displayMatchTime() {
	processFootballProcedures('READ_CLOCK',null);
	//processFootballProcedures('READ-MATCH-AND-POPULATE',null);
}
function initialiseForm(whatToProcess, dataToProcess)
{
	switch (whatToProcess) {
	case 'TIME':
		if(match_data) {
			if(document.getElementById('match_time_hdr')) {
				document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
					millisToMinutesAndSeconds(match_data.clock.matchTotalMilliSeconds);
			}
		}
		
		break;
	}
}
function uploadFormDataToSessionObjects(whatToProcess)
{
	var formData = new FormData();
	var url_path;

	$('input, select, textarea').each(
		function(index){  
			if($(this).is("select")) {
				formData.append($(this).attr('id'),$('#' + $(this).attr('id') + ' option:selected').val());  
			} else {
				formData.append($(this).attr('id'),$(this).val());  
			}	
		}
	);
	
	switch(whatToProcess.toUpperCase()) {
	case 'RESET_MATCH':
		url_path = 'reset_and_upload_match_setup_data';
		break;
	case 'SAVE_MATCH':
		url_path = 'upload_match_setup_data';
		break;
	}
	
	$.ajax({    
		headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')},
        url : url_path,     
        data : formData,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',     
        success : function(data) {

        	switch(whatToProcess.toUpperCase()) {
        	case 'RESET_MATCH':
        		alert('Match has been reset');
        		processWaitingButtonSpinner('END_WAIT_TIMER');
        		break;
        	case 'SAVE_MATCH':
        		document.setup_form.method = 'post';
        		document.setup_form.action = 'setup_to_match';
        	   	document.setup_form.submit();
        		break;
        	}
        	
        },    
        error: function(jqXHR, textStatus, errorThrown) {    
        	console.error('Error occurred in ' + whatToProcess + ' with error description: ' + textStatus, errorThrown);
        }
    
    });		
	
}
function processUserSelectionData(whatToProcess,dataToProcess){
	//alert(whatToProcess);
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		switch (dataToProcess) {
		case '-':
			processFootballProcedures('RESET-ALL-ANIMATION');
			break;
		/*case 65:
			processFootballProcedures('SWAP_DATA');
			break;	*/
		case 32:
			processFootballProcedures('CLEAR-ALL');
			break;
		case 189:
			if(confirm('It will Also Delete Your Preview from Directory...\r\n\r\n Are You Sure To Animate Out?') == true){
				processFootballProcedures('ANIMATE-OUT');
			}
			break;
		case 65:
			addItemsToList('ATTENDENCE-OPTIONS',null);
			break;
		case 83:
			processFootballProcedures('POPULATE-AIFF');
			break;
		case 68:
			addItemsToList('ADDITIONAL_TIME-OPTIONS',null);
			break;
		case 70:
			addItemsToList('FREETEXT-OPTIONS',null);
			break;
		case 90:
			processFootballProcedures('POPULATE-WELCOME');
			break;
		case 88:
			processFootballProcedures('POPULATE-SECURITY');
			break;
		case 67:
			processFootballProcedures('POPULATE-NOTICE');
			break;								
		case 187:
			processFootballProcedures('ANIMATE-OUT-SCOREBUG');
			break;
		case 113:
			processFootballProcedures('POPULATE-MATCHID');
			break;
		case 114:
			processFootballProcedures('POPULATE-MATCHSTATS');
			break;
		case 115:
			processFootballProcedures('POPULATE-MATCHSUBS');
			break;
		case 116:
			addItemsToList('TEAMLIST-OPTIONS',null);
			break;
		case 117:
			addItemsToList('NAMESUPER-CARD-OPTIONS',null);
			addItemsToList('POPULATE-PLAYER',null);
			break;
		case 71:
			addItemsToList('GOAL-GFX-OPTIONS',null);
			addItemsToList('POPULATE-PLAYER',null);
			break;
		case 89: 'Y - PENALTY'
			processFootballProcedures('POPULATE-PENALTY');
			break;						
		/*case 114:
			processFootballProcedures('POPULATE-MATCHSUBS');
			break;	
		case 115:
			processFootballProcedures('POPULATE-SCORELINE');
			break;*/	
		case 112:
			processFootballProcedures('POPULATE-TOURNAMENT_LOGO');
			break;
		case 77: 'm'
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#football_div").hide();
			addItemsToList('SCOREBUG-SUBSTITUTION-OPTIONS',null);
			break;
		case 84: 'T - OFFICIALS'
			processFootballProcedures('POPULATE-OFFICIALS');
			break;
		case 80: 'p - OFFICIALS'
			addItemsToList('PENALTY-OPTION',null);
			//addItemsToList('PENALTY-BUTTON');
			break;
		case 85: 'u - PENALTY CHANGE'
			processFootballProcedures('POPULATE-CHANGE_PENALTY');
			break;				
		/*case 69:'e'
			addItemsToList('EXTRA-TIME_OPTION',null);
			break;
		case 82:'r'
			addItemsToList('TIME-EXTRA_OPTION',null);
			break;
		*/		
		}
		
		break;
	}
}
function processUserSelection(whichInput)
{	
	var error_msg = '';

	switch ($(whichInput).attr('name')) {
	case 'load_scene_btn':
		/*if(checkEmpty($('#vizIPAddress'),'IP Address Blank') == false
			|| checkEmpty($('#vizPortNumber'),'Port Number Blank') == false) {
			return false;
		}*/
	  	document.initialise_form.submit();
		break;
	case 'penalty_graphics_btn':
		addItemsToList('PENALTY-OPTION',match_data);
		break;
	case 'cancel_match_setup_btn':
		document.setup_form.method = 'post';
		document.setup_form.action = 'setup_to_match';
	   	document.setup_form.submit();
		break;
	case 'matchFileName':
		if(document.getElementById('matchFileName').value) {
			document.getElementById('matchFileName').value = 
				document.getElementById('matchFileName').value.replace('.xml','') + '.json';
		}
		break;
	case 'load_match_btn':
		processWaitingButtonSpinner('START_WAIT_TIMER');
		processFootballProcedures('LOAD_MATCH',$('#select_football_matches option:selected'));
		break;
	case 'cancel_graphics_btn':
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#select_event_div").show();
		$("#match_configuration").show();
		$("#football_div").show();
		break;
	case 'cancel_penalty_btn':
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#select_event_div").show();
		$("#match_configuration").show();
		$("#football_div").show();
		break;
	case 'selectTeam': case 'selectCaptianWicketKeeper':
		addItemsToList('POPULATE-PLAYER',match_data);
		break;
	case 'populate_extra_time_btn':
		processFootballProcedures('POPULATE-EXTRA_TIME');
		break;
	case 'populate_time_extra_btn':
		processFootballProcedures('POPULATE-TIME_EXTRA');
		break;
	case 'populate_scorebug_subs_btn':
		processFootballProcedures('POPULATE-SCOREBUG-SUBS');
		break;
	case 'populate_teamlist_btn':
		processFootballProcedures('POPULATE-TEAMLIST');
		break;	
	case 'populate_namesuper_card_btn':
		processFootballProcedures('POPULATE-L3-NAMESUPER-CARD');
		break;
	case 'populate_goal_btn':
		processFootballProcedures('POPULATE-GOAL');
		break;
	case 'populate_att_btn':
		processFootballProcedures('POPULATE-ATTENDENCE');
		break;
	case 'populate_additional_btn':
		processFootballProcedures('POPULATE-ADDITIONAL');
		break;
	case 'populate_free_btn':
		processFootballProcedures('POPULATE-FREE');
		break;
	default:
		if($(whichInput).attr('id').includes('_btn') && $(whichInput).attr('id').split('_').length >= 4) {
    		switch ($(whichInput).attr('id').split('_')[1]) {
    		case 'increment':
    			$('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
					+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val(
					parseInt($('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
					+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val()) + 1
				);
				break;
    		case 'decrement':
				if(parseInt($('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
					+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val()) > 0) {
	    			
					$('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
						+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val(
						parseInt($('#' + $(whichInput).attr('id').split('_')[0] + '_' + $(whichInput).attr('id').split('_')[2] 
						+ '_' + $(whichInput).attr('id').split('_')[3] + '_txt').val()) - 1
					);
					
				}
				break;
			}		
			console.log(whichInput)		
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processFootballProcedures('LOG_STAT',whichInput);
		}
		break;					
	}
}
function processFootballProcedures(whatToProcess, whichInput)
{
	var value_to_process; 
	
	switch(whatToProcess) {
	case 'READ-MATCH-AND-POPULATE':
		if(match_data == null){
			return false;
		}
		value_to_process = $('#matchFileTimeStamp').val();
		break;
	case 'LOG_STAT':
		value_to_process = whichInput.id;
		break;	
	case 'LOAD_MATCH':
		value_to_process = whichInput.val();
		//alert(value_to_process);
		break;
	case 'READ_CLOCK':
		valueToProcess = $('#matchFileTimeStamp').val();
		break;
	case 'POPULATE-EXTRA_TIME': case 'POPULATE-TIME_EXTRA':
		value_to_process = $('#selectExtratime').val();
		break;
	case 'POPULATE-SCOREBUG-SUBS':
		value_to_process = $('#selectStatsType option:selected').val() + ',' + $('#selectTeam option:selected').val();
		break;
	case 'POPULATE-TEAMLIST':
		value_to_process = $('#selectTeam option:selected').val();
		break;	
	case 'POPULATE-L3-NAMESUPER-CARD':
		value_to_process = $('#selectTeam option:selected').val() + ',' + 
		$('#selectCaptainWicketKeeper option:selected').val() + ',' + $('#selectPlayer option:selected').val() ;
		break;
	case 'POPULATE-GOAL':
		value_to_process = 	$('#selectTeam option:selected').val() + ',' + $('#selectPlayer option:selected').val();
		break;
	case 'POPULATE-ATTENDENCE': case 'POPULATE-ADDITIONAL':
		value_to_process = 	$('#selectattendence').val();
		break;
	case 'POPULATE-FREE':
		value_to_process = 	$('#selectFirst').val() + ',' + $('#selectSecond').val();
		break;
	case 'POPULATE-PENALTY':
		value_to_process = 	'D:\DOAD_In_House_Everest\Everest_Sports\Everest_Intercontinental_2024\Scene\BigScreen_New.sum';
		break;							
	}

	$.ajax({    
        type : 'Get',     
        url : 'processFootballProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + value_to_process, 
        dataType : 'json',
        success : function(data) {
			match_data = data;
        	switch(whatToProcess) {
			case 'SWAP_DATA':
				alert('values are swaped');
				break;
			case 'READ-MATCH-AND-POPULATE':
				if(data){
					/*if($('#matchFileTimeStamp').val() != data.matchFileTimeStamp) {
						document.getElementById('matchFileTimeStamp').value = data.matchFileTimeStamp;
						processFootballProcedures('READ-MATCH-AND-POPULATE',null);
						match_data = data;
					}*/
					processFootballProcedures('READ-MATCH-AND-POPULATE',null);
						match_data = data;
					addItemsToList('LOAD_EVENTS',data);
					//processFootballProcedures('READ-MATCH-AND-POPULATE',null);
					
					if(data.clock) {
						if(document.getElementById('match_time_hdr')) {
							document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
								millisToMinutesAndSeconds(data.clock.matchTotalMilliSeconds);
						}
					}
				}
				break;
			case 'READ_CLOCK':
				if(match_data.clock) {
					if(document.getElementById('match_time_hdr')) {
						document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
							millisToMinutesAndSeconds(match_data.clock.matchTotalMilliSeconds);
					}
				}
				
				if(data){
					session_match = data;
					addItemsToList('LOAD_MATCH',data);
					addItemsToList('LOAD_EVENTS',data);
					document.getElementById('football_div').style.display = '';
					document.getElementById('select_event_div').style.display = '';
					/*if($('#matchFileTimeStamp').val() != data.matchFileTimeStamp) {
						document.getElementById('matchFileTimeStamp').value = data.matchFileTimeStamp;
						session_match = data;
						addItemsToList('LOAD_MATCH',data);
						addItemsToList('LOAD_EVENTS',data);
						document.getElementById('football_div').style.display = '';
						document.getElementById('select_event_div').style.display = '';
					}*/
				}
				break;	
			case 'LOAD_MATCH':
				match_data = data;
				addItemsToList('LOAD_EVENTS',data);
				//processFootballProcedures('READ-MATCH-AND-POPULATE',null);
				document.getElementById('football_div').style.display = '';
				document.getElementById('select_event_div').style.display = '';
				setInterval(displayMatchTime, 500);
				break;
				
        	case 'POPULATE-MATCHID': case 'POPULATE-SCORELINE': case 'POPULATE-TOURNAMENT_LOGO': case 'POPULATE-MATCHSUBS':
        	case 'POPULATE-SCORELINE': case 'POPULATE-TIME_EXTRA': case 'POPULATE-MATCHSTATS': case 'POPULATE-TEAMLIST':
        	case 'POPULATE-L3-NAMESUPER-CARD': case 'POPULATE-SCOREBUG-SUBS': case 'POPULATE-OFFICIALS': case 'POPULATE-PENALTY':
        	case 'POPULATE-GOAL': case 'POPULATE-ATTENDENCE': case 'POPULATE-AIFF': case 'POPULATE-ADDITIONAL':
        	case 'POPULATE-FREE': case 'POPULATE-WELCOME': case 'POPULATE-SECURITY': case 'POPULATE-NOTICE':
        		if(confirm('Animate In?') == true){
					switch(whatToProcess){
					case 'POPULATE-PENALTY':
						processFootballProcedures('ANIMATE-IN-PENALTY');			
						break;	
					case 'POPULATE-WELCOME':
						processFootballProcedures('ANIMATE-IN-WELCOME');
						break;
					case 'POPULATE-SECURITY':
						processFootballProcedures('ANIMATE-IN-SECURITY');
						break;
					case 'POPULATE-NOTICE':
						processFootballProcedures('ANIMATE-IN-NOTICE');
						break;		
					case 'POPULATE-FREE':
						processFootballProcedures('ANIMATE-IN-FREE');
						break;
					case 'POPULATE-ADDITIONAL':
						processFootballProcedures('ANIMATE-IN-ADDITIONAL');
						break;
					case 'POPULATE-AIFF':
						processFootballProcedures('ANIMATE-IN-AIFF');
						break;
					case 'POPULATE-ATTENDENCE':
						processFootballProcedures('ANIMATE-IN-ATTENDENCE');
						break;
					case 'POPULATE-GOAL':
						processFootballProcedures('ANIMATE-IN-GOAL');
						break;	
					case 'POPULATE-OFFICIALS':
						processFootballProcedures('ANIMATE-IN-OFFICIALS');
						break;	
					case 'POPULATE-SCOREBUG-SUBS':
						processFootballProcedures('ANIMATE-IN-SCOREBUG-SUBS');		
						break;
					case 'POPULATE-L3-NAMESUPER-CARD':
						processFootballProcedures('ANIMATE-IN-NAMESUPER-CARD');		
						break;
					case 'POPULATE-MATCHSTATS':
						processFootballProcedures('ANIMATE-IN-MATCHSTATS');		
						break;
					case 'POPULATE-TIME_EXTRA':
						processFootballProcedures('ANIMATE-IN-TIME_EXTRA');		
						break;
					case 'POPULATE-MATCHSUBS':
						processFootballProcedures('ANIMATE-IN-MATCHSUBS');		
						break;	
					case 'POPULATE-TEAMLIST':
						processFootballProcedures('ANIMATE-IN-TEAMLIST');	
						break;		
					case 'POPULATE-MATCHID':
						processFootballProcedures('ANIMATE-IN-MATCHID');		
						break;
					case 'POPULATE-SCORELINE':
						processFootballProcedures('ANIMATE-IN-SCORELINE');		
						break;
					case 'POPULATE-TOURNAMENT_LOGO':
						processFootballProcedures('ANIMATE-IN-TOURNAMENT_LOGO');		
						break;
					}
				}
				break;
        	}
    		processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error: function(jqXHR, textStatus, errorThrown) {    
        	console.error('Error occurred in ' + whatToProcess + ' with error description: ' + textStatus, errorThrown);
        }    
	});
}
function addItemsToList(whatToProcess, dataToProcess)
{
	var div,row,header_text,event_text,select,option,tr,th,thead,text,table,tbody,teamName;
	var cellCount=0;
	switch (whatToProcess) {
	
	case 'POPULATE-PLAYER':
		$('#selectPlayer').empty();
		if(match_data.homeTeamId ==  $('#selectTeam option:selected').val()){
			match_data.homeSquad.forEach(function(hs,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
	                value: hs.playerId,
	                text: hs.jersey_number + ' - ' + hs.full_name
		        }))					
			});
			match_data.homeSubstitutes.forEach(function(hsub,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
					value: hsub.playerId,
					text: hsub.jersey_number + ' - ' + hsub.full_name + ' (SUB)'
				}))
			});
		}
		else {
			match_data.awaySquad.forEach(function(as,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
	                value: as.playerId,
	                text: as.jersey_number + ' - ' + as.full_name
		        }))					
			});
			match_data.awaySubstitutes.forEach(function(asub,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
					value: asub.playerId,
					text: asub.jersey_number + ' - ' + asub.full_name + ' (SUB)'
				}))
			});
		}
		
		break;
	case 'NAMESUPER-CARD-OPTIONS':
		$('#select_graphic_options_div').empty();
	
		header_text = document.createElement('h6');
		header_text.innerHTML = 'Select Graphic Options';
		document.getElementById('select_graphic_options_div').appendChild(header_text);
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');

		table.appendChild(tbody);
		document.getElementById('select_graphic_options_div').appendChild(table);
		
		row = tbody.insertRow(tbody.rows.length);
		
		select = document.createElement('select');
		select.id = 'selectTeam';
		select.name = select.id;
		
		option = document.createElement('option');
		option.value = match_data.homeTeamId;
		option.text = match_data.homeTeam.teamName1;
		select.appendChild(option);
		
		option = document.createElement('option');
		option.value = match_data.awayTeamId;
		option.text = match_data.awayTeam.teamName1;
		select.appendChild(option);
	
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(cellCount).appendChild(select);
		cellCount = cellCount + 1;

		select = document.createElement('select');
		select.style = 'width:100px';
		select.id = 'selectCaptainWicketKeeper';
		select.name = select.id;
		
		option = document.createElement('option');
		option.value = 'yellow';
		option.text = 'YELLOW CARD';
		select.appendChild(option);
		
		option = document.createElement('option');
		option.value = 'red';
		option.text = 'RED CARD';
		select.appendChild(option);
		
		option = document.createElement('option');
		option.value = 'yellow_red';
		option.text = '2YELLOW/RED';
		select.appendChild(option);
		
		select.setAttribute('onchange',"processUserSelection(this)");
		row.insertCell(cellCount).appendChild(select);
		cellCount = cellCount + 1;
		
		select = document.createElement('select');
		select.style = 'width:100px';
		select.id = 'selectPlayer';
		select.name = select.id;
		
		row.insertCell(cellCount).appendChild(select);
		cellCount = cellCount + 1;
		
		option = document.createElement('input');
   	 	option.type = 'button';
	    option.name = 'populate_namesuper_card_btn';
	    option.value = 'Populate Namesuper Card';
	    option.id = option.name;
	    option.setAttribute('onclick',"processUserSelection(this)");
	    
	    div = document.createElement('div');
	    div.append(option);

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_graphics_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.append(option);
	    
	    row.insertCell(cellCount).appendChild(div);
	    cellCount = cellCount + 1;
	    
		document.getElementById('select_graphic_options_div').style.display = '';
		break;
	
	case 'SCOREBUG-SUBSTITUTION-OPTIONS': case 'TEAMLIST-OPTIONS': case 'GOAL-GFX-OPTIONS':
	case 'ATTENDENCE-OPTIONS': case 'ADDITIONAL_TIME-OPTIONS': case 'FREETEXT-OPTIONS':
	
		switch ($('#selectedBroadcaster').val().toUpperCase()) {
		case 'FOOTBALL':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				case 'FREETEXT-OPTIONS':
					select = document.createElement('input');
					select.type = "text";
					select.id = 'selectFirst';
					select.value = '';

					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					select = document.createElement('input');
					select.type = "text";
					select.id = 'selectSecond';
					select.value = '';

					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					option = document.createElement('input');
		    		option.type = 'button';
		    		
		    		option.name = 'populate_free_btn';
		    		option.value = 'Populate Free';
		    		
		    		option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
				    
				    row.insertCell(cellCount).appendChild(div);
				    cellCount = cellCount + 1;
					
				    
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(cellCount).appendChild(div);
				    cellCount = cellCount + 1;
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'ATTENDENCE-OPTIONS': case 'ADDITIONAL_TIME-OPTIONS':
					select = document.createElement('input');
					select.type = "text";
					select.id = 'selectattendence';
					select.value = '';

					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					switch(whatToProcess){
						case 'ATTENDENCE-OPTIONS':
						option = document.createElement('input');
			    		option.type = 'button';
			    		
			    		option.name = 'populate_att_btn';
			    		option.value = 'Populate Att';
			    		
			    		option.id = option.name;
					    option.setAttribute('onclick',"processUserSelection(this)");
					    
					    div = document.createElement('div');
					    div.append(option);
					    
					    row.insertCell(cellCount).appendChild(div);
					    cellCount = cellCount + 1;
						break;
						case 'ADDITIONAL_TIME-OPTIONS':
						option = document.createElement('input');
			    		option.type = 'button';
			    		
			    		option.name = 'populate_additional_btn';
			    		option.value = 'Populate Additional';
			    		
			    		option.id = option.name;
					    option.setAttribute('onclick',"processUserSelection(this)");
					    
					    div = document.createElement('div');
					    div.append(option);
					    
					    row.insertCell(cellCount).appendChild(div);
					    cellCount = cellCount + 1;
						break;
					}
					
				    
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(cellCount).appendChild(div);
				    cellCount = cellCount + 1;
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'GOAL-GFX-OPTIONS':
					select = document.createElement('select');
					select.id = 'selectTeam';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = match_data.homeTeamId;
					option.text = match_data.homeTeam.teamName1;
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = match_data.awayTeamId;
					option.text = match_data.awayTeam.teamName1;
					select.appendChild(option);
					
					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					select = document.createElement('select');
					select.style = 'width:100px';
					select.id = 'selectPlayer';
					select.name = select.id;
					
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					option = document.createElement('input');
		    		option.type = 'button';
		    		
		    		option.name = 'populate_goal_btn';
		    		option.value = 'Populate Goal';
		    		
		    		option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
				    
				    row.insertCell(cellCount).appendChild(div);
				    cellCount = cellCount + 1;
				    
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(cellCount).appendChild(div);
				    cellCount = cellCount + 1;
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'SCOREBUG-SUBSTITUTION-OPTIONS': case 'TEAMLIST-OPTIONS':				
					switch ($('#selectedBroadcaster').val().toUpperCase()){
					case 'FOOTBALL':
						select = document.createElement('select');
						select.id = 'selectTeam';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = match_data.homeTeamId;
						option.text = match_data.homeTeam.teamName1;
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = match_data.awayTeamId;
						option.text = match_data.awayTeam.teamName1;
						select.appendChild(option);
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						switch(whatToProcess){
							case 'SCOREBUG-SUBSTITUTION-OPTIONS':
							select = document.createElement('select');
							select.id = 'selectStatsType';
							select.name = select.id;
							
							option = document.createElement('option');
							option.value = 'single';
							option.text = 'Single Substitution';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'double';
							option.text = 'Double Substitution';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'triple';
							option.text = 'Triple Substitution';
							select.appendChild(option);
							
						    select.setAttribute('onchange',"processUserSelection(this)");
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
							
							option = document.createElement('input');
				    		option.type = 'button';
				    		
				    		option.name = 'populate_scorebug_subs_btn';
				    		option.value = 'Populate Subs';
				    		
				    		option.id = option.name;
						    option.setAttribute('onclick',"processUserSelection(this)");
						    
						    div = document.createElement('div');
						    div.append(option);
						    
						    row.insertCell(cellCount).appendChild(div);
						    cellCount = cellCount + 1;
					    
							break;
							
						case 'TEAMLIST-OPTIONS':
							option = document.createElement('input');
				    		option.type = 'button';
				    		
				    		option.name = 'populate_teamlist_btn';
				    		option.value = 'Populate List';
				    		
				    		option.id = option.name;
						    option.setAttribute('onclick',"processUserSelection(this)");
						    
						    div = document.createElement('div');
						    div.append(option);
						    
						    row.insertCell(cellCount).appendChild(div);
						    cellCount = cellCount + 1;
							break;	
						}
						
						
					    
						option = document.createElement('input');
						option.type = 'button';
						option.name = 'cancel_graphics_btn';
						option.id = option.name;
						option.value = 'Cancel';
						option.setAttribute('onclick','processUserSelection(this)');
				
					    div.append(option);
					    
					    row.insertCell(cellCount).appendChild(div);
					    cellCount = cellCount + 1;
					    
						document.getElementById('select_graphic_options_div').style.display = '';
						break;
					
					}
					break;
			}
			
			break;
		}
		break;
	
	case 'TIME-EXTRA_OPTION':
		switch ($('#selectedBroadcaster').val()) {
		case 'FOOTBALL':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				case 'TIME-EXTRA_OPTION':
					select = document.createElement('input');
					select.type = "text";
					select.id = 'selectExtratime';
					select.value = '';
					
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					break;
				}
					
			break;
			}
			
			option = document.createElement('input');
		    option.type = 'button';
			switch (whatToProcess) {
			case 'TIME-EXTRA_OPTION':
				option.name = 'populate_time_extra_btn';
		    	option.value = 'Populate Extra Time';
				break;
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';

		break;
		
	case 'EXTRA-TIME_OPTION':
		switch ($('#selectedBroadcaster').val()) {
		case 'FOOTBALL':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				case 'EXTRA-TIME_OPTION':
					select = document.createElement('input');
					select.type = "text";
					select.id = 'selectExtratime';
					select.value = '';
					
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					break;
				}
					
			break;
			}
			
			option = document.createElement('input');
		    option.type = 'button';
			switch (whatToProcess) {
			case 'EXTRA-TIME_OPTION':
				option.name = 'populate_extra_time_btn';
		    	option.value = 'Populate Extra Time';
				break;
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';

		break;
	case 'PENALTY-OPTION':
		
		$('#select_graphic_options_div').empty();
		
		header_text = document.createElement('h6');
		header_text.innerHTML = 'Select Graphic Options';
		document.getElementById('select_graphic_options_div').appendChild(header_text);
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');

		table.appendChild(tbody);
		document.getElementById('select_graphic_options_div').appendChild(table);
		
		row = tbody.insertRow(tbody.rows.length);

		for(var i=1; i<=2; i++) {
			div = document.createElement('div');
			div.style = 'text-align:center;';
			switch(i){
			case 1:
				text = 'home';
				break;
			case 2:
				text = 'away';
				break;
			}
			div.id = text + '_penalties_div';
			for(var j=0; j<=5; j++) {
				switch(j){
				case 0: case 3:
					header_text = document.createElement('label');
					header_text.htmlFor = div.id;
					option = document.createElement('input');
					option.type = "button";
					option.style = 'text-align:center;';
					option.setAttribute('onclick','processUserSelection(this)');
					switch(j){
					case 0:
						header_text.innerHTML = text.toUpperCase() + ' Hits: ';
						option.id = text + '_increment_penalties_hit_btn';
						break;
					case 3:
						header_text.innerHTML = 'Misses: ';
						option.id = text + '_increment_penalties_miss_btn';
						break;
					}
					option.value = "+";
					div.appendChild(header_text).appendChild(option);
					break;
				case 1: case 4:
	    			option = document.createElement('input');
					option.type = 'text';
					switch(j){
					case 1:
						option.id = text + '_penalties_hit_txt';
						break;
					case 4:
						option.id = text + '_penalties_miss_txt';
						break;
					}
					option.value = '0';
					option.style = 'width:10%;text-align:center;';
					div.appendChild(option);
					break;
				case 2: case 5:
					option = document.createElement('input');
					option.type = "button";
					option.style = 'text-align:center;';
					option.setAttribute('onclick','processUserSelection(this)');
					switch(j){
					case 2:
						option.id = text + '_decrement_penalties_hit_btn';
						break;
					case 5:
						option.id = text + '_decrement_penalties_miss_btn';
						break;
					}
					option.value = "-";
					div.appendChild(option);
				    div.append(document.createElement('br'));
					break;
				}
			}
			row.insertCell(i-1).appendChild(div);
		}

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_penalty_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.appendChild(option);

		row.insertCell(2).appendChild(div);
		
		table.appendChild(tbody);
		
		document.getElementById('select_graphic_options_div').style.display = '';
		break;
			
	case 'LOAD_EVENTS':
		
		$('#select_event_div').empty();
		
		if (dataToProcess)
		{
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			tbody = document.createElement('tbody');
			
			table.appendChild(tbody);
			document.getElementById('select_event_div').appendChild(table);

			row = tbody.insertRow(tbody.rows.length);
			header_text = document.createElement('h6');
			header_text.id = 'match_time_hdr';
			header_text.innerHTML = 'Match Time: 00:00';
			row.insertCell(0).appendChild(header_text);
			
			if(dataToProcess.events != null && dataToProcess.events.length > 0) {
				max_cols = dataToProcess.events.length;
				if (max_cols > 20) {
					max_cols = 20;
				}
				header_text = document.createElement('h6');
				for(var i = 0; i < max_cols; i++) {
					if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventPlayerId != 0){
						dataToProcess.homeSquad.forEach(function(hs,index,arr){
							if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventPlayerId == hs.playerId){
								playerName = ' {'+ hs.ticker_name +'}' ;
							}				
						});
						dataToProcess.awaySquad.forEach(function(as,index,arr){
							if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventPlayerId == as.playerId){
								playerName = ' {'+ as.ticker_name +'}';
							}				
						});
						dataToProcess.homeSubstitutes.forEach(function(hsub,index,arr){
							if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventPlayerId == hsub.playerId){
								playerName = ' {'+ hsub.ticker_name +'}';
							}		
						});
						dataToProcess.awaySubstitutes.forEach(function(asub,index,arr){
							if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventPlayerId == asub.playerId){
								playerName = ' {'+ asub.ticker_name +'}';
							}			
						});
					}else{
						playerName = '';
					}
					
					if(dataToProcess.events[(dataToProcess.events.length - 1) - i].eventType) {
						if(header_text.innerHTML) {
							header_text.innerHTML = header_text.innerHTML + ', ' + dataToProcess.events[(dataToProcess.events.length - 1) - i].eventType.replaceAll('_',' ') + playerName; // .match(/\b(\w)/g).join('')
						} else {
							header_text.innerHTML = dataToProcess.events[(dataToProcess.events.length - 1) - i].eventType.replaceAll('_',' ') + playerName; // .match(/\b(\w)/g).join('')
						}
					}
				}
				header_text.innerHTML = 'Events: ' + header_text.innerHTML;
				row.insertCell(1).appendChild(header_text);
				
			}
			
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			thead = document.createElement('thead');
			tbody = document.createElement('tbody');
			tr = document.createElement('tr');
			for (var j = 0; j <= 1; j++) {
			    th = document.createElement('th'); // Column
				th.scope = 'col';
			    switch (j) {
				case 0:
				    th.innerHTML = dataToProcess.homeTeam.teamName1 + ': ' + dataToProcess.homeTeamScore ;
					break;
				case 1:
					th.innerHTML = dataToProcess.awayTeam.teamName1 + ': ' + dataToProcess.awayTeamScore ;
					break;
				}	
				tr.appendChild(th);
			}
			thead.appendChild(tr);
			table.appendChild(thead);
			document.getElementById('select_event_div').appendChild(table);
			
			// Penalty Section
			
		
			/*tbody = document.createElement('tbody');
			for(var i = 0; i <= dataToProcess.homeSquad.length - 1; i++) {
				row = tbody.insertRow(tbody.rows.length);
				for(var j = 1; j <= 2; j++) {
					text = document.createElement('text');
					switch(j){
					case 1:
						text.innerHTML = dataToProcess.homeSquad[i].jersey_number + ': ' + dataToProcess.homeSquad[i].full_name ;
						break;
					case 2:
						text.innerHTML = dataToProcess.awaySquad[i].jersey_number + ': ' + dataToProcess.awaySquad[i].full_name ;
						break;
					}
					
					row.insertCell(j-1).appendChild(text);
				}
			}				
			row = tbody.insertRow(tbody.rows.length);
			header_text = document.createElement('header');
			header_text.innerHTML = 'Substitutes';
			row.insertCell(0).appendChild(header_text);*/
			
			/*max_cols = dataToProcess.homeSubstitutes.length;
			if(dataToProcess.homeSubstitutes.length < dataToProcess.awaySubstitutes.length) {
				max_cols = dataToProcess.awaySubstitutes.length;
			}
			
			for(var i = 0; i <= max_cols-1; i++) {
				
				row = tbody.insertRow(tbody.rows.length);
				
				for(var j = 0; j <= 1; j++) {
					
					addSelect = false;
					
					switch(j) {
					case 0:
						if(i <= parseInt(dataToProcess.homeSubstitutes.length - 1)) {
							addSelect = true;
						}
						break;
					case 1:
						if(i <= parseInt(dataToProcess.awaySubstitutes.length - 1)) {
							addSelect = true;
						}
						break;
					}

					text = document.createElement('label');
					
					if(addSelect == true) {
					
						switch(j){
						case 0:
							text.innerHTML = dataToProcess.homeSubstitutes[i].jersey_number + ': ' + dataToProcess.homeSubstitutes[i].full_name;
							break;
							
						case 1:
							text.innerHTML = dataToProcess.awaySubstitutes[i].jersey_number + ': ' + dataToProcess.awaySubstitutes[i].full_name;
							break;
						}
						text.setAttribute('style','cursor: pointer;');
					
					}	
				
					row.insertCell(j).appendChild(text);
				
				}
			}				

			table.appendChild(tbody);
			document.getElementById('select_event_div').appendChild(table);*/
		}
	    
		break;
	}
}
function removeSelectDuplicates(select_id)
{
	var this_list = {};
	$("select[id='" + select_id + "'] > option").each(function () {
	    if(this_list[this.text]) {
	        $(this).remove();
	    } else {
	        this_list[this.text] = this.value;
	    }
	});
}
function checkEmpty(inputBox,textToShow) {

	var name = $(inputBox).attr('id');
	
	document.getElementById(name + '-validation').innerHTML = '';
	document.getElementById(name + '-validation').style.display = 'none';
	$(inputBox).css('border','');
	if(document.getElementById(name).value.trim() == '') {
		$(inputBox).css('border','#E11E26 2px solid');
		document.getElementById(name + '-validation').innerHTML = textToShow + ' required';
		document.getElementById(name + '-validation').style.display = '';
		document.getElementById(name).focus({preventScroll:false});
		return false;
	}
	return true;	
}	
