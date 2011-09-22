
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
	this.bgcolor = "rgb(201,199,224)";
	this.strokecolor = "#000";
	this.data = "";
	this.msg = "";
	this.from=-1;
	this.to=-1;
	this.success;
	this.enable=true;
	this.ajaxtype="";
	this.click = -1;
	this.timespan = 2000;
	this.timer = null;
	this.gameover = false;
	this.win = -1;
}

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
		
		$("#n"+i).attr("src",src);
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
			
			$("#n"+i).attr("src",src);
		}
	}
};

ChessBoard.prototype.SetSelectedNode = function(){
	this.container.find("img").each(function(){
		$(this).css({border:"0px"});
	});	
	if(this.to!=-1){
		this.container.find("img[id='n"+this.to+"']").css({border:"1px solid red"});
	}
	else{
		if(this.from!=-1){
			this.container.find("img[id='n"+this.from+"']").css({border:"1px solid red"});
		}
	}
};

ChessBoard.prototype.Ajax = function(){
	var self = this;
	if(this.enable==false){
		this.msg.text("please wait and try again later");
		return;
	}
	this.enable = false;
	
	$.ajax({
		type:"POST",
		cache:false,
		url:config.webroot+"/HandleClickBoard",
		data:(function(){
			if(self.ajaxtype=="click"){
				return {"type":"click",
					"room":config.room,
					"clickNode":self.click,
					"isRed":config.type==1,
					"data":self.data};
			}
			else if(self.ajaxtype=="timer"){
				return {"type":"timer","room":config.room};
			}
		})(),
		success:function(json){
			self.enable = true;
			eval("var json="+json);
			
			if(json.type=="click"){
				var eventList = json.data;
				$.each(eventList,function(i,ev){
					self.msg.text(ev.message);
					var play = (self.data!=ev.chessBoardData);
					if(play){
						self.SetBoardByData2(self.data,ev.chessBoardData);
						self.data=ev.chessBoardData;						
					}					
					self.from = ev.fromNode;
					self.to = ev.toNode;
					self.SetSelectedNode();						
				});				
			}
			else if(json.type=="timer"){
				if(self.data!=json.data){
					self.SetBoardByData2(self.data,json.data);
					self.data=json.data;					
				}
				self.from = json.from;
				self.to = json.to;
				self.SetSelectedNode(json.from, json.to);				
			}		
			
		},
		error:function(ex){
			self.enable = true;
			self.msg.text("error and try again later!");
			//alert("error:"+ex.responseText)
		}		
	})
}
ChessBoard.prototype.setTimer = function(){
	var self = this;
	this.timer = setInterval(function(){
		self.ajaxtype="timer";
		self.Ajax();
	},this.timespan);
}
ChessBoard.prototype.HandleClick = function(id){
	this.ajaxtype = "click";
	this.click = id.replace("n","");
	this.Ajax();
}
ChessBoard.prototype.DrawBoard = function(){
	var self = this;
	this.container.empty();
	var win = $(window);
	this.container.css({position:"absolute",top:"50%",left:"50%"});
	var bc = this.sideLength;
	var space = this.qzspace;
    
     var xsum = (config.type==0) ? 10 : 9;
    
     var ysum = 19 - xsum;
     var  qpheight= (ysum + 1) * bc;
     var qpwidth = (xsum + 1) * bc;
     this.container.css({width:qpwidth+"px",height:qpheight+"px","margin-left":-1*qpwidth/2+"px","margin-top":-1*qpheight/2+"px"});
     var r0 = (bc - space) / 2; //棋子半径 
     var drawObj = new drawing(this.container[0], { antialias: true, fillcolor: this.bgcolor });// 作图对象
     
     if (config.type>0) {
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
 
     var r0 = (bc - space) / 2;
     
     for (var i = 0; i < xsum; i++) {
         for (var j = 0; j < ysum; j++) {     
             $("<img mytype='node' />").attr({src:config.imgroot + "/k.png",id:function(){
            	 if(config.type==0){//红方在左边
            		 return "n"+ (8+i*9-j);
            	 }
            	 else if(config.type==1){//红方在下边
            		 return "n"+(89-i-j*9);
            	 }
            	 else{//红方在上边
            		 return "n"+(i+j*9);
            	 } 
             }})
             .css({position:"absolute",width:2*r0+"px",height:2*r0+"px",zIndex:2,
                     left: bc + bc * i - r0 + "px", top: bc + bc * j - r0 + "px"
             }).click(function(){
            	 if(config.type>0){
            		 self.HandleClick($(this).attr("id"));
            	 }
            	 else{
            		 self.msg.text("look and don't speak")
            	 }
             }).appendTo(drawObj.el);
         }
     }  
     this.msg = $("<span style='position:absolute;z-index:2;'/>").text("hello");
    this.container.prepend(this.msg);
}


