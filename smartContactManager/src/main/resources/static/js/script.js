/**
 * 
 */

 
 console.log("this is script file")
 
 const togglebar = () =>
 { 
	 if($(".sidebar").is(":visible")) //close the sidebar if displayed on l;eft side
	 {
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	 }
	 else{
		 //close the sidebar if displayed on l;eft side
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	 }
	 
 }