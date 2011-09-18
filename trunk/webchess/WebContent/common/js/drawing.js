/* Dmo Drawing 
* v0.0667
* David Morrison
* http://www.dmowebstudios.com/drawing.php
* dmo@dmowebstudios.com
* Last Update: 7/25/06
* All rights reserved
*/
document.onselectstart = new Function("return false;");

var drawing = function(el, attr) { // fillcolor, antialias, unit
	this.el = el;
	this.attr = attr;
	this.drawing = {};
	this.units = "px"; // coming soon
	
	this.clear = function (p) {
		while (p.childNodes.length > 0) {
			p.removeChild(p.childNodes[p.childNodes.length-1]);
		}
		if (p.childNodes.length > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	this.loadjsv = function(jsv, scale) { 
		if(this.drawing && jsv.primitives && jsv.primitives.length) {
			for(var l=0;l<jsv.primitives.length;l++) {
				if(jsv.primitives[l].type == "line") {
				    this.line(this.drawing, jsv.primitives[l]);				
				} else if (jsv.primitives[l].type == "polyline") {
					this.polyline(this.drawing, jsv.primitives[l]);	
				} else if (jsv.primitives[l].type == "circle") {
					this.circle(this.drawing, jsv.primitives[l]);	
				} else if (jsv.primitives[l].type == "oval") {
					this.oval(this.drawing, jsv.primitives[l]);	
				} else if (jsv.primitives[l].type == "rectangle") {
					this.rectangle(this.drawing, jsv.primitives[l]);	
				} else if (jsv.primitives[l].type == "polygon") {
				    var p = jsv.primitives[l];
					p.points = this.scalepoints(p.points, scale)
					this.polygon(this.drawing, p);	
				}
			}
		}
	};

	this.scalepoints = function(points, scale) {
		var newpoints = [];
		var oldpoints = points.split(",");
		for(var p=0;p<oldpoints.length;p++) {
			newpoints[p] = oldpoints[p] * scale;
		}
		return newpoints.join(",");
	};	
	
	if(document.createElementNS) {
		this.svgNS = "http://www.w3.org/2000/svg";
		this.svg = document.createElementNS(this.svgNS, "svg");
		this.svgSupported = (this.svg.x != null);
	} else {
		this.svgSupported = false;
	}
  
	if(this.svgSupported) {
		this.init = function() {
			var c = document.createElementNS(this.svgNS, "svg");
			if (this.attr.fillcolor) { this.el.style.backgroundColor = this.attr.fillcolor; }
			this.el.appendChild(c);
			return c;
		};
		this.line = function(p, attr) {
			var el = document.createElementNS(this.svgNS, "line");
			el.setAttribute("shape-rendering", (this.attr.antialias) ? "auto" : "optimizeSpeed");
			el.set = function(attr) {
				if (attr.x1) { this.setAttribute("x1", attr.x1); }
				if (attr.y1) { this.setAttribute("y1", attr.y1); }
				if (attr.x2) { this.setAttribute("x2", attr.x2); }
				if (attr.y2) { this.setAttribute("y2", attr.y2); }
				if (attr.strokewidth) { this.setAttribute("stroke-width", 	attr.strokewidth ); }
				if (attr.strokecolor) { this.setAttribute("stroke", 		attr.strokecolor ); }
				if (attr.linecap)     { this.setAttribute("stroke-linecap", attr.linecap ); }
			};
			el.style.position = "absolute";
			el.setAttribute("stroke-width", "1px" );
			el.setAttribute("stroke",       "#000000" );
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
		this.polyline = function (p, attr) { 
			var el = document.createElementNS(this.svgNS, "polyline");
			el.setAttribute("shape-rendering", (this.attr.antialias) ? "auto" : "optimizeSpeed");
			el.set = function(attr) {
				if (attr.points) { this.setAttribute("points", attr.points); }
				if (attr.strokewidth) { this.setAttribute("stroke-width", 	attr.strokewidth ); }
				if (attr.strokecolor) { this.setAttribute("stroke", 		attr.strokecolor ); }
				if (attr.linecap)     { this.setAttribute("stroke-linecap", attr.linecap ); }
			};
			el.style.position = "absolute";
			el.setAttribute("stroke-width", "1px" );
			el.setAttribute("stroke",       "#000000" );
			el.setAttribute("fill",       	"none" );
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		
		};
		this.circle = function (p, attr) { // attr: { x1, y1, r, strokewidth, strokecolor, fillcolor }
			var el = document.createElementNS(this.svgNS, "circle");
			el.setAttribute("shape-rendering", (this.attr.antialias) ? "auto" : "optimizeSpeed");
			el.set = function(attr) {
				if (attr.x) { this.setAttribute("cx", 	attr.x); }
				if (attr.y) { this.setAttribute("cy", 	attr.y); }
				if (attr.r) { this.setAttribute("r", 		attr.r); }
				if (attr.strokewidth) { this.setAttribute("stroke-width", 	attr.strokewidth ); }
				if (attr.strokecolor) { this.setAttribute("stroke", 		attr.strokecolor ); }
				if (attr.fillcolor)   { this.setAttribute("fill", 			attr.fillcolor); }
			};
			el.style.position = "absolute";
			el.setAttribute("stroke-width", "1px" ); 
			el.setAttribute("stroke",       "#000000" ); 
			el.setAttribute("fill",       	"none" ); 
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
		this.oval = function (p, attr) { // attr: { x1, y1, x2, y2, strokewidth, strokecolor, fillcolor } 
			var el = document.createElementNS(this.svgNS, "ellipse");
			el.setAttribute("shape-rendering", (this.attr.antialias) ? "auto" : "optimizeSpeed");
			el.set = function(attr) {
				if (attr.x) 	{ this.setAttribute("cx", attr.x); }
				if (attr.y) 	{ this.setAttribute("cy", attr.y); }
				if (attr.rx) 	{ this.setAttribute("rx", attr.rx); }
				if (attr.ry) 	{ this.setAttribute("ry", attr.ry); }
				if (attr.strokewidth) 	{ this.setAttribute("stroke-width", 	attr.strokewidth ); }
				if (attr.strokecolor) 	{ this.setAttribute("stroke", 			attr.strokecolor ); }
				if (attr.fillcolor)   	{ this.setAttribute("fill", 			attr.fillcolor); }
				if (attr.rotate)   		{ this.setAttribute("transform", 		"rotate(" + attr.rotate + " " + parseInt(attr.x) + " " + parseInt(attr.y) + " )" ); }
			};
			el.style.position = "absolute";
			el.setAttribute("stroke-width", "1px" );
			el.setAttribute("stroke",       "#000000" ); 
			el.setAttribute("fill",       	"none" ); 
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
		this.rectangle = function (p, attr) { // x, y, w, h, r
			var el = document.createElementNS(this.svgNS, "rect");
			el.setAttribute("shape-rendering", (this.attr.antialias) ? "auto" : "optimizeSpeed");
			el.set = function(attr) {
				if (attr.x) { this.setAttribute("x", attr.x); }
				if (attr.y) { this.setAttribute("y", attr.y); }
				if (attr.w) { this.setAttribute("width", attr.w); }
				if (attr.h) { this.setAttribute("height", attr.h); }
				if (attr.strokewidth) { this.setAttribute("stroke-width", 	attr.strokewidth ); }
				if (attr.strokecolor) { this.setAttribute("stroke", 		attr.strokecolor ); }
				if (attr.fillcolor)   { this.setAttribute("fill", 			attr.fillcolor); }
				if (attr.r)   		  { this.setAttribute("rx", 			attr.r); }
				// if (attr.rotate)      { this.setAttribute("transform", 		"rotate(" + attr.rotate + " " + (parseInt(attr.x) + parseInt(attr.w)/2)  + " " + (parseInt(attr.y) + parseInt(attr.h)/2) + " )" ); }
			};
			el.style.position = "absolute";
			el.setAttribute("stroke-width", "1px" ); 
			el.setAttribute("stroke",       "#000000" ); 
			el.setAttribute("fill",       	"none" );  
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		
		};
		this.polygon = function (p, attr) {
			var el = document.createElementNS(this.svgNS, "polygon");
			el.setAttribute("shape-rendering", (this.attr.antialias) ? "auto" : "optimizeSpeed");
			el.set = function(attr) {
				if (attr.points) { this.setAttribute("points", attr.points); }
				if (attr.strokewidth) { this.setAttribute("stroke-width", 	attr.strokewidth ); }
				if (attr.strokecolor) { this.setAttribute("stroke", 		attr.strokecolor ); }
				if (attr.linecap)     { this.setAttribute("stroke-linecap", attr.linecap ); }
				if (attr.fillcolor)   { this.setAttribute("fill", 			attr.fillcolor); }
			};
			el.style.position = "absolute";
			el.setAttribute("stroke-width", "1px" );
			el.setAttribute("stroke",       "#000000" );
			el.setAttribute("fill",       	"none" );
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;

		};
	} else if (document.createStyleSheet) {
		this.init = function() {
			document.namespaces.add("v", "urn:schemas-microsoft-com:vml");
			var style = document.createStyleSheet();
			style.addRule('v\\:*', "behavior: url(#default#VML);");
			if (this.attr.antialias) { style.addRule('v\\:*', "antialias: "+this.attr.antialias+";"); }
			if (this.attr.fillcolor) { this.el.style.backgroundColor = this.attr.fillcolor; }
			return this.el;    
		};
		this.line = function(p, attr) { // x1, y1, x2, y2, strokewidth, strokecolor
			var el = document.createElement("v:line");
			el.set = function(attr) {
				if (attr.x1 && attr.y1) { this.setAttribute("to", 	attr.x1 + "," + attr.y1); }
				if (attr.x2 && attr.y2) { this.setAttribute("from", 	attr.x2 + "," + attr.y2); }
				if (attr.strokewidth) { this.setAttribute("strokeweight", attr.strokewidth); }
				if (attr.strokecolor) { this.setAttribute("strokecolor", attr.strokecolor); }
				if (attr.linecap) {
					if (this.getElementsByTagName("stroke").length == 1) {
						var s = this.getElementsByTagName("stroke")[0];
						if (s) { s.setAttribute("endcap", attr.linecap); }
					} else {
						var s = document.createElement("v:stroke");
						s.setAttribute("endcap", attr.linecap);
						this.appendChild(s);
					}
				}
			};
			el.style.position = "absolute";
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
		this.polyline = function (p, attr) { // points, strokewidth, strokecolor
			var el = document.createElement("v:polyline");
			el.set = function(attr) {
				if (attr.points) { this.setAttribute("points", attr.points); }
				if (attr.strokewidth) { this.setAttribute("strokeweight", attr.strokewidth); }
				if (attr.strokecolor) { this.setAttribute("strokecolor", attr.strokecolor); }
				if (attr.linecap) {
					if (this.getElementsByTagName("stroke").length == 1) {
						var s = this.getElementsByTagName("stroke")[0];
						if (s) { s.setAttribute("endcap", attr.linecap); }
					} else {
						var s = document.createElement("v:stroke");
						s.setAttribute("endcap", attr.linecap);
						this.appendChild(s);
					}
				}
			};
			el.style.position = "absolute";
			el.setAttribute("filled", "false");
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
		this.circle = function (p, attr) { // attr: { x y, r, strokewidth, strokecolor, fillcolor }
			var el = document.createElement("v:oval");
			el.set = function(attr) {
				if (attr.x && attr.r) { this.style.left = (parseInt(attr.x, 10) - parseInt(attr.r, 10)) + attr.r.slice(-2); }
				if (attr.y && attr.r) { this.style.top = (parseInt(attr.y, 10) - parseInt(attr.r, 10)) + attr.r.slice(-2); } //attr.r.slice(-2)
				if (attr.r) { this.style.width = (parseInt(attr.r, 10) * 2) + attr.r.slice(-2); }
				if (attr.r) { this.style.height = (parseInt(attr.r, 10) * 2) + attr.r.slice(-2); }
				if (attr.strokewidth) { this.setAttribute("strokeweight", attr.strokewidth); }
				if (attr.strokecolor) { this.setAttribute("strokecolor", attr.strokecolor); }
				if (attr.fillcolor) { 
					this.setAttribute("filled", "true");
					this.setAttribute("fillcolor", attr.fillcolor); 
				}
			};
			el.style.position = "absolute";
			el.setAttribute("filled", "false");
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
		this.oval = function (p, attr) {  // attr: {  x, y, rx, ry, strokewidth, strokecolor, fillcolor
			var el = document.createElement("v:oval");
			el.set = function(attr) {
				if (attr.x && attr.rx) { this.style.left = 	(parseInt(attr.x, 10) - parseInt(attr.rx, 10)) + attr.rx.slice(-2); }
				if (attr.x && attr.ry) { this.style.top = 	(parseInt(attr.y, 10) - parseInt(attr.ry, 10)) + attr.ry.slice(-2); }
				if (attr.rx) { this.style.width = 	( parseInt(attr.rx, 10) * 2) + attr.rx.slice(-2); }
				if (attr.ry) { this.style.height = ( parseInt(attr.ry, 10) * 2) + attr.ry.slice(-2); }
				if (attr.strokewidth) { this.setAttribute("strokeweight", attr.strokewidth); }
				if (attr.strokecolor) { this.setAttribute("strokecolor", attr.strokecolor); }
				if (attr.fillcolor) { this.setAttribute("filled", "true"); this.setAttribute("fillcolor", attr.fillcolor); }
				if (attr.rotate) { this.setAttribute("rotation", attr.rotate );	}
			};
			el.style.position = "absolute";
			el.setAttribute("filled", "false");
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
		this.rectangle = function (p, attr) { // x, y, w, h, r
			var el = (attr.r) ? document.createElement("v:roundrect") : document.createElement("v:rect") ;
			el.set = function(attr) {
				if (attr.x) { this.style.left = attr.x; }
				if (attr.y) { this.style.top = attr.y; }
				if (attr.w) { this.style.width = attr.w; }
				if (attr.h) { this.style.height = attr.h; }
				// if (attr.rotate) 		{ this.setAttribute("rotation", attr.rotate );	} // no workie
				if (attr.r) { this.setAttribute("arcsize", (parseInt(attr.r)/(parseInt(attr.w)).toFixed(2))); }
				if (attr.strokewidth) 	{ this.setAttribute("strokeweight", attr.strokewidth); }
				if (attr.strokecolor) 	{ this.setAttribute("strokecolor", attr.strokecolor); }
				if (attr.fillcolor) 	{ this.setAttribute("filled", "true"); this.setAttribute("fillcolor", attr.fillcolor); }
			};
			el.style.position = "absolute";
			el.setAttribute("filled", 	"false");
			el.setAttribute("arcsize", 	"0%");
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
		this.polygon = function (p, attr) { 
			var el = document.createElement("v:polyline");
			el.set = function(attr) {
				if (attr.points) { 
				 var p = attr.points.split(" ");
				 p.push(p[0]);
				 p.push(p[1]);
				 this.setAttribute("points", p.join(" "));
				}
				if (attr.strokewidth) { this.setAttribute("strokeweight", attr.strokewidth); }
				if (attr.strokecolor) { this.setAttribute("strokecolor", attr.strokecolor); }
				if (attr.fillcolor) { this.setAttribute("filled", "true"); this.setAttribute("fillcolor", attr.fillcolor); }
				if (attr.linecap) {
					if (this.getElementsByTagName("stroke").length == 1) {
						var s = this.getElementsByTagName("stroke")[0];
						if (s) { s.setAttribute("endcap", attr.linecap); }
					} else {
						var s = document.createElement("v:stroke");
						s.setAttribute("endcap", attr.linecap);
						this.appendChild(s);
					}
				}
			};
			el.style.position = "absolute";
			el.setAttribute("filled", "false");
			el.set(attr);
			if (p && p.appendChild) { p.appendChild(el); }
			return el;
		};
	} else {
		this.init = function() {
			return false;    
		}    
		alert("Drawing Not Supported!");
	}
	this.drawing = this.init();
}