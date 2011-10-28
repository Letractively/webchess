
function ChessBoard(container){
	this.container = null;
	if(container){
		this.container = container;
	}
	else{
		this.container=$("<div/>").appendTo("body").end();
	}
	
	this.sideLength = 50;
	this.qzspace = 6;
	this.bgcolor = config.qpColor;
	this.strokecolor = config.qpLineColor;
	this.data = "";
	this.msg = "";
	this.roomNum="";
	this.from=-1;
	this.to=-1;
	this.successMove=false;
	this.successHold=false;
	this.enable=true;
	this.ajaxtype="";
	this.click = -1;
	this.timespan = 2000;
	this.timer = null;
	this.gameover = false;
	this.win = -1;
	this.timerPause = false;
	this.isRedToGo = true;
	this.pauseComputer = false;
};

ChessBoard.prototype.HandleGameOver = function(){
	
};

ChessBoard.prototype.setTimer = function(){
	var self = this;
	this.timer = setInterval(function(){
		if(!self.timerPause){
			self.ajaxtype="timer";
			try{
				self.Ajax();
			}catch(ex){};
		}
	},this.timespan);
};

ChessBoard.prototype.KillTimer = function(){
	if(this.timer){
		window.clearInterval(this.timer);
	}
};

ChessBoard.prototype.PauseTimer = function(){
	this.timerPause=true;
};

ChessBoard.prototype.ResetTimer = function(){
	this.timerPause=false;
};

ChessBoard.prototype.SetBoardByData = function(){
	for(var i=0;i<this.data.length;i++){
		var ch = this.data.substring(i,i+1);
		var charCode = ch.charCodeAt(0);
		var src = config.imgroot+"/";
		if(charCode<91){
			src+=ch.toLowerCase()+"1.png";
		}
		else{
			src+=ch+".png";
		}
		
		jQuery("#n"+i).attr("src",src);
	}
};

ChessBoard.prototype.SetBoardByData2 = function(data,newdata){
	for(var i=0;i<data.length;i++){
		var ch = data.substring(i,i+1);
		var ch2 = newdata.substring(i,i+1);
		if(ch!=ch2){
			var charCode = ch2.charCodeAt(0);
			var src = config.imgroot+"/";
			if(charCode<91){
				src+=ch2.toLowerCase()+"1.png";
			}
			else{
				src+=ch2+".png";
			}
			
			jQuery("#n"+i).attr("src",src);
		}
	}
};

ChessBoard.prototype.SetSelectedNode = function(){
	this.container.find("img").each(function(){
		jQuery(this).css({border:"0px"});
	});
	if(this.successMove==true){
		this.container.find("img[id='n"+this.to+"']").css({border:"1px solid red"});
	}
	if(this.successHold==true){
		this.container.find("img[id='n"+this.from+"']").css({border:"1px solid red"});
	}
};

ChessBoard.prototype.Ajax = function(){
	var self = this;
	if(this.enable==false){
		this.msg.text("please wait and try again later");
		return;
	}
	this.enable = false;
	
	jQuery.ajax({
		type:"POST",
		cache:false,
		url:config.webroot+"/HandleClickBoard",
		data:(function(){
			return {
				"type":self.ajaxtype,
				"room":config.roomNum,
				"clickNode":self.click,
				"isRed":config.seatType==1,
				"data":self.data,
				"isVsComputer":config.isVsComputer
				};
		})(),
		success:function(json){			
			eval("var json="+json);	
			self.isRedToGo = json.isRedToGo;
			if(self.ajaxtype=="changeroom"){
				self.container.empty();
				self.DrawBoard();				
				self.trigger(self.eChangeRoom);
			}
			self.HandleEventList(json.data);			
			self.ComputerPlayHandle();
		},
		error:function(ex){
			self.msg.text("error and try again later!");
		},
		complete:function(){
			self.enable = true;
		}
	});
};

ChessBoard.prototype.ComputerPlayHandle = function(){
	if(config.isVsComputer===true){	
		if(config.isComputerAuto===true){			
			this.ComputerPlay();
		}
		else{
			if(((config.seatType==1) && (this.isRedToGo===false))
					||((config.seatType==2) && (this.isRedToGo===true))){
				if(!this.pauseComputer){
					this.ComputerPlay();
				}
				else{
					this.pauseComputer = false;
				}
			}
		}
	}
};

ChessBoard.prototype.ComputerPlay = function(){
	this.ajaxtype = "computer";
	this.enable = true;
	this.Ajax();
};

ChessBoard.prototype.HandleClick = function(id){
	this.ajaxtype = "click";
	this.click = id.replace("n","");
	this.Ajax();
};
ChessBoard.prototype.ChangeRoom = function(){
	this.data = "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk";
	this.ajaxtype="changeroom";
	this.Ajax();
};
ChessBoard.prototype.Undo = function(){
	this.ajaxtype = "undo";
	this.Ajax();
};
ChessBoard.prototype.Redo = function(){
	this.ajaxtype="redo";
	this.Ajax();
};

ChessBoard.prototype.bind = function(name,handle){
	$(this).bind(name,handle);
	return this;
};

ChessBoard.prototype.unbind = function(name){
	$(this).unbind(name);
	return this;
};

ChessBoard.prototype.trigger = function(name){
	$(this).trigger(name,this);
	return this;
};

ChessBoard.prototype.eChangeRoom = "ChangeRoom";
ChessBoard.prototype.HandleEventList = function(eventList){
	var self = this;
	jQuery.each(eventList,function(i,ev){
		var eventName = ev.eventName;
		self[eventName](ev);
	});
};
ChessBoard.prototype.EventCommonHandle = function(ev){
	this.msg.text(ev.message);	
	this.from = ev.fromNode;
	this.to = ev.toNode;
	this.successHold=(ev.isSuccessHold=="true");
	this.successMove=(ev.isSuccessMove=="true");
	this.SetSelectedNode();
};
ChessBoard.prototype.CHECKRESULT_NEEDUPDATE = function(ev){	
	this.SetBoardByData2(this.data,ev.chessBoardData);
	this.data=ev.chessBoardData;	
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_NEEDWAITOPPONENT_RED = function(ev){
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_NEEDWAITOPPONENT_BLACK = function(ev){
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_CLICKWRONGNODE_RED = function(ev){
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_CLICKWRONGNODE_BLACK = function(ev){
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_FIRSTHOLDNODE_RED = function(ev){	
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_FIRSTHOLDNODE_BLACK = function(ev){	
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_CHANGEHOLDNODE_RED = function(ev){	
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_CHANGEHOLDNODE_BLACK = function(ev){	
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_HOLDSAMENODE_RED = function(ev){	
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_HOLDSAMENODE_BLACK = function(ev){	
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_PLAYOK_RED = function(ev){
	this.SetBoardByData2(this.data,ev.chessBoardData);
	this.data=ev.chessBoardData;
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_PLAYOK_BLACK = function(ev){	
	this.SetBoardByData2(this.data,ev.chessBoardData);
	this.data=ev.chessBoardData;
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_WIN_RED = function(ev){
	this.HandleGameOver();
};

ChessBoard.prototype.CHECKRESULT_WIN_BLACK = function(ev){
	this.HandleGameOver();
};

ChessBoard.prototype.CHECKRESULT_DOGFALL = function(ev){
	this.HandleGameOver();
};

ChessBoard.prototype.CHECKRESULT_DENYPLAY_RED = function(ev){
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_DENYPLAY_BLACK = function(ev){
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_INIT_CHECK = function(ev){
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_UNDO_RED = function(ev){
	
	this.SetBoardByData2(this.data,ev.chessBoardData);
	this.data=ev.chessBoardData;	
	this.EventCommonHandle(ev);
	this.pauseComputer = true;
};

ChessBoard.prototype.CHECKRESULT_UNDO_BLACK = function(ev){	
	this.SetBoardByData2(this.data,ev.chessBoardData);
	this.data=ev.chessBoardData;	
	this.EventCommonHandle(ev);
	this.pauseComputer = true;
};

ChessBoard.prototype.CHECKRESULT_REDO_RED = function(ev){	
	this.SetBoardByData2(this.data,ev.chessBoardData);
	this.data=ev.chessBoardData;	
	this.EventCommonHandle(ev);
	this.pauseComputer = true;
};

ChessBoard.prototype.CHECKRESULT_REDO_BLACK = function(ev){	
	this.SetBoardByData2(this.data,ev.chessBoardData);
	this.data=ev.chessBoardData;	
	this.EventCommonHandle(ev);
	this.pauseComputer = true;
};

ChessBoard.prototype.CHECKRESULT_TOKILLKING_RED = function(ev){
	this.EventCommonHandle(ev);
};

ChessBoard.prototype.CHECKRESULT_TOKILLKING_BLACK = function(ev){
	this.EventCommonHandle(ev);
};


ChessBoard.prototype.DrawBoard = function(){
	var self = this;
	this.container.empty();
	this.container.css({position:"absolute",top:"50%",left:"50%"});
	var bc = this.sideLength;
	var space = this.qzspace;
    
     var xsum = (config.seatType==0) ? 10 : 9;
    
     var ysum = 19 - xsum;
     var  qpheight= (ysum + 1) * bc;
     var qpwidth = (xsum + 1) * bc;
     this.container.css({width:qpwidth+"px",height:qpheight+"px","margin-left":-1*qpwidth/2+"px","margin-top":-1*qpheight/2+"px"});
     
     var drawObj = new drawing(this.container[0], { antialias: true, fillcolor: this.bgcolor });// 作图对象
     
     if (config.seatType>0) {
         for (var x = 0; x < 10; x++) {
             drawObj.line(drawObj.drawing, {
                 x1: bc + "px", y1: bc + x * bc + "px", x2: 9 * bc + "px", y2: bc + x * bc + "px",
                 strokewidth: "1px", strokecolor: this.strokecolor
             }).style.zIndex = 1;
         }
         for (var x = 0; x < 9; x++) {
             drawObj.line(drawObj.drawing, {
                 y1: bc + "px", x1: bc + x * bc + "px", y2: 5 * bc + "px", x2: bc + x * bc + "px",
                 strokewidth: "1px", strokecolor: this.strokecolor
             }).style.zIndex = 1;
             var t = 6;
             if (x == 0 || x == 8) t = 5;
             drawObj.line(drawObj.drawing, {
                 y1: t * bc + "px", x1: bc + x * bc + "px", y2: 10 * bc + "px", x2: bc + x * bc + "px",
                 strokewidth: "1px", strokecolor: this.strokecolor
             }).style.zIndex = 1;
         }
         drawObj.line(drawObj.drawing, {
             x1: 4*bc + "px", y1: bc  + "px", x2: 6 * bc + "px", y2: 3* bc + "px",
             strokewidth: "1px", strokecolor: this.strokecolor
         }).style.zIndex = 1;
         drawObj.line(drawObj.drawing, {
             x1: 6*bc + "px", y1: bc  + "px", x2: 4 * bc + "px", y2: 3* bc + "px",
             strokewidth: "1px", strokecolor: this.strokecolor
         }).style.zIndex = 1;
         drawObj.line(drawObj.drawing, {
             x1: 4*bc + "px", y1:8* bc  + "px", x2: 6 * bc + "px", y2: 10* bc + "px",
             strokewidth: "1px", strokecolor: this.strokecolor
         }).style.zIndex = 1;
         drawObj.line(drawObj.drawing, {
             x1: 6*bc + "px", y1: 8*bc  + "px", x2: 4 * bc + "px", y2: 10* bc + "px",
             strokewidth: "1px", strokecolor: this.strokecolor
         }).style.zIndex = 1;
     }
     else {
         for (var x = 0; x < 10; x++) {
             drawObj.line(drawObj.drawing, {
                 y1: bc + "px", x1: bc + x * bc + "px", y2: 9 * bc + "px", x2: bc + x * bc + "px",
                 strokewidth: "1px", strokecolor: this.strokecolor
             }).style.zIndex = 1;
         }
         for (var x = 0; x < 9; x++) {
             drawObj.line(drawObj.drawing, {
                 x1: bc + "px", y1: bc + x * bc + "px", x2: 5 * bc + "px", y2: bc + x * bc + "px",
                 strokewidth: "1px", strokecolor: this.strokecolor
             }).style.zIndex = 1;
             var t = 6;
             if (x == 0 || x == 8) t = 5;
             drawObj.line(drawObj.drawing, {
                 x1: t * bc + "px", y1: bc + x * bc + "px", x2: 10 * bc + "px", y2: bc + x * bc + "px",
                 strokewidth: "1px", strokecolor: this.strokecolor
             }).style.zIndex = 1;
         }
         drawObj.line(drawObj.drawing, {
             x1: bc + "px", y1: 4*bc  + "px", x2: 3 * bc + "px", y2: 6* bc + "px",
             strokewidth: "1px", strokecolor: this.strokecolor
         }).style.zIndex = 1;
         drawObj.line(drawObj.drawing, {
             x1: bc + "px", y1:6*bc  + "px", x2: 3* bc + "px", y2: 4* bc + "px",
             strokewidth: "1px", strokecolor: this.strokecolor
         }).style.zIndex = 1;
         drawObj.line(drawObj.drawing, {
             x1:8* bc + "px", y1: 4*bc  + "px", x2: 10 * bc + "px", y2: 6* bc + "px",
             strokewidth: "1px", strokecolor: this.strokecolor
         }).style.zIndex = 1;
         drawObj.line(drawObj.drawing, {
             x1: 8*bc + "px", y1:6*bc  + "px", x2: 10* bc + "px", y2: 4* bc + "px",
             strokewidth: "1px", strokecolor: this.strokecolor
         }).style.zIndex = 1;
     }     
 
     var r0 = (bc - space) / 2;//棋子半径 
     
     for (var i = 0; i < xsum; i++) {
         for (var j = 0; j < ysum; j++) {     
             jQuery("<img mytype='node' />").attr({src:config.imgroot + "/k.png",id:function(){
            	 if(config.seatType==0){//红方在左边
            		 return "n"+ (8+i*9-j);
            	 }
            	 else if(config.seatType==1){//红方在下边
            		 return "n"+(89-i-j*9);
            	 }
            	 else{//红方在上边
            		 return "n"+(i+j*9);
            	 } 
             }})
             .css({position:"absolute",width:2*r0+"px",height:2*r0+"px",zIndex:2,
                     left: bc + bc * i - r0 + "px", top: bc + bc * j - r0 + "px"
             }).click(function(){
            	 if(config.seatType>0){
            		 self.HandleClick(jQuery(this).attr("id"));
            	 }
            	 else{
            		 self.msg.text("look and don't speak");
            	 }
             }).appendTo(drawObj.el);
         }
     }  
};


