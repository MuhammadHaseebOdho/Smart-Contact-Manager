console.log("hellp");
const toggleSidebar=()=>{
  if ($(".sidebar").is(":visible")){
      $(".sidebar").css("display","none");
      $(".content").css("margin-left","0%");
  }else {
      $(".sidebar").css("display","block");
      $(".content").css("margin-left","20%");
  }
};



const search=()=>{
    let query=$("#search-input").val();
    let url=`http://localhost:8080/search/${query}`;
    console.log("func calling");
    if(query==""){
        $("#search-result").hide();
    }else{
        
        fetch(url)
            .then((response)=>{
                return response.json();
            })
            .then((data)=>{
                console.log(data);    
                let text="<div class='list-group'>";
                data.forEach(element=>{
                    text+=`<a href="/user/viewcontact/${element.contactId}" class='list-group-item list-group-action'> ${element.name} </a>`
                });
                text+=`</div>`;
                $(".search-result").html(text);
                $(".search-result").show();
        });
    }
};