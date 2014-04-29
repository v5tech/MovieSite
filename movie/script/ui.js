/* Function:
 * 1. Login, render user's name on the div[id=userinfo]
 *    URL: http://host:port/MovieSite/user?email={email}
 * 2. Load User's Movie
 *	  URL: http://host:port/MovieSite/movie?userID={userID}
 * 3. Load Recommended Movie
 *    URL: http://host:port/MovieSite/recommend?userID={userID}
 */
  var login = function(){
  	var emailString = $('email').getValue();
  	var password = $('password').getValue();
      new Ajax.Request('/MovieSite/user',
  		{
    		method:'get',
    		parameters: {email: emailString},
    		onSuccess: function(transport){
      		var response = transport.responseText || "no response text";
      			var data = response.evalJSON();
				var id = data.id;
				var name = data.name;
				var email = data.email;
				//hide login form
				var login_form = $('login_form');
				login_form.hide();
				
				//render user info
				var userinfo = $('userinfo');
				var info = "Hi, "+name+"("+email+"), your user id is "+id;
				console.debug(info);
				userinfo.innerHTML = "Hi, "+name+"("+email+"), your id is "+id;
				
				//show movie table
				var movieTable = $('movies');
				movieTable.show();
				loadUserMovies(id);
				loadRecommendMovies(id);
    		},
    		onFailure: function(){ alert('Something went wrong...'); }
  		});
      
    }
    var loadUserMovies = function(user_id){
    	new Ajax.Request('/MovieSite/movies',
  		{
    		method:'get',
    		parameters: {userID: user_id},
    		onSuccess: function(transport){
      		var response = transport.responseText || "no response text";
//      			console.debug("response", response);
      			var data = response.evalJSON();
//      			console.debug("data", data);
				renderMovies(data, "my_movies_loading", "userMovies");
    		},
    		onFailure: function(){ alert('Something went wrong...'); }
  		});
    }
    var loadRecommendMovies = function(user_id){
    	new Ajax.Request('/MovieSite/recommend',
  		{
    		method:'get',
    		parameters: {userID: user_id, format:"json"},
    		onSuccess: function(transport){
      		var response = transport.responseText || "no response text";
//      			console.debug("response", response);
      			var data = response.evalJSON();
//      			console.debug("data", data);
				renderMovies(data, "recommend_movies_loading", "recommendatedMovies");
    		},
    		onFailure: function(){ alert('Something went wrong...'); }
  		});
    }
    var renderMovies = function(data, loading_id, dom_id){
    	if(Object.isArray(data)){
    		console.debug("data is array");
    		var loading = $(loading_id);
    		var node = $(dom_id);
//    		var fragment = document.createDocumentFragment();
    		var table = document.createElement("table");
    		Element.addClassName(table, "movie_table");
    		var tablehead = document.createElement("thead");
    		var head_content = '<tr class="odd_row"><td width="40%"><b>Movie Name</b></td><td width="20%"><b>Published Year</td><td width="35%"><b>Movie Type</td><td width="10%"><b>Score</td></tr>';
    		Element.insert(tablehead, head_content);
//    		tablehead.innerHTML='<tr><td width="40%">Movie Name</td><td width="25%">Published Year</td><td width="25%">Movie Type</td><td width="10%">Score</td></tr>';
    		table.appendChild(tablehead);
    		var tablebody = document.createElement("tbody");
    		for (var index = 0, len = data.length; index < len; ++index) {
  				var item = data[index];
  				var movie = item.movie;
  				var score = item.score;
  				var tr = document.createElement("tr");
  				if(index%2 == 0){
  					tr.style.backgroundColor='#DDDDEE';
  				}
  				var content = "<td>"+movie.name+"</td><td>"+movie.publish_year+"</td><td>"+movie.type+"</td><td>"+score+"</td>";	
  				Element.insert(tr, content);
//  			tr.innerHTML = "<td>"+movie.name+"</td><td>"+movie.publish_year+"</td><td>"+movie.type+"</td><td>"+score+"</td>";
  				tablebody.appendChild(tr);
			}
			table.appendChild(tablebody);
			node.appendChild(table);
			loading.hide();

   		} else {
   			console.debug("data is not array");
   		}
    }
