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
	 
 };
 
 const search = () => {
	 
	// console.log("searching");
	 
	 let query =$("#search-input").val();
	 
	 if(query == "")
	 {
		 $(".search-result").hide();
	 }
	 else{
		 console.log(query);
		 
		 let url = `http://localhost:8080/search/${query}`;  //back tick used here to complete senetence = `
		 
		 fetch(url).then((response) => {
			  return response.json();
		  })
		  .then((data) => {
			  
			  console.log(data);
			  
			  let text = `<div class = 'list-group'>`
			  
			  data.forEach((contact) =>{
			  text += `<a href ='/user/${contact.cid}/contact' class = 'list-group-item list-group-action'> ${contact.name}</a>`
		   });  
			  text+= `</div>`;
			  $(".search-result").html(text);
			  $(".search-result").show();
			  
			 
		  });
		  }
};