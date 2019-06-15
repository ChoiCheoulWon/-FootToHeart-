/*
 * Modified by Yoonah Lee, GC shin, reference following
 * 2018/3/23 Kyuho Kim(ekyuho@gmail.com)
*/

var express = require('express');
var app = express();
fs = require('fs');
mysql = require('mysql');

var connection = mysql.createConnection({
    host: 'localhost',
    user: 'me',
    password: 'mypassword',
    database: 'mydb'
})
connection.connect();
/* ( I changed database table ; sensor -> temp )
function insert_sensor(temperature,ip) {
  obj = {};
  obj.temperature = temperature
  obj.ip = ip.replace(/^.*:/, '')

  var query = connection.query('insert into sensors set ?', obj, function(err, rows, cols) {
    if (err) throw err;
    console.log("database insertion ok= %j", obj);
    //console.log("databse insertion ok = %j %j %j", obj.seq, obj.type, obj.ip);
  });
}
*/
function error_check(nickname,callback){
	var sql = mysql.format('select sum(left_num) as left_sum, sum(right_num) as right_sum from temp where nickname = ?  and today_date = date(current_date());',nickname);
	var query = connection.query(sql,function(err,result){
		if (err) throw err;
		console.log("Error check/// left_sum is "+result[0].left_sum + " right_sum is "+result[0].right_sum);
		if ((result[0].left_sum - result[0].right_sum >=30 )|| (result[0].right_sum - result[0].left_sum >=30 )){
			console.log("Sensor is broken");
			return 1;
		}
		else{
			return -1;
		}
	});
	console.log(query);
}
function insert_temp(nickname,left_num,right_num) {

	obj = {};
	obj.nickname = nickname;
	obj.left_num = left_num;
	obj.right_num = right_num;

	var today = new Date();
	var dd = today.getDate();


	var mm = today.getMonth()+1; 
	var yyyy = today.getFullYear();
	obj.today_date = yyyy+'-'+ mm +'-'+dd;
	console.log(obj.today_date);
	console.log("nickname is ? left_num is ? right_num is ?",nickname,left_num,right_num);

/*
	var sql = mysql.format('insert into temp(nickname,left_num,right_num,today_date) values (? , ? , ?, date(current_date()));',nickname,left_num,right_num);
	var query = connection.query(sql,function(err,result){
		if(err) throw err;
		console.log("inserted right~");
	});*/
		
	var query = connection.query('insert into temp set ? ', obj,function(err,rows,cols){
		if(err) throw err;
		console.log("database insertion ok=%j",obj);
	});

	var sql = mysql.format('insert ignore into final ( nickname, total, time) select nickname,count(*),today_date from temp where nickname = ? and today_date = DATE( CURRENT_DATE()) group by nickname,time;',nickname);
	var query = connection.query(sql,function(err,result){
		if(err) throw err;
		console.log('insert loop');
		console.log(result[0]);
	});

	var sql2 = mysql.format(' update ignore final set total = ( select count(*) from temp where nickname = ? and today_date = DATE(CURRENT_DATE())) where nickname = ?  and time = DATE(CURRENT_DATE());',[nickname,nickname]);
        var query2 = connection.query(sql2,function(err,result2){
                if(err) throw err;
		console.log('update loop');
                console.log(result2[0]);
        });
	var sql2 = mysql.format(' update ignore final set success = 1 where total >= 2400 ;');
	var query2 = connection.query(sql2,function(err,result2){
		if(err) throw err;
		console.log('update success');
		console.log(result2[0]);
	});
}
app.get('/getfriend/:firstuser',function(request,res,next){ 
	var firstuser = request.params.firstuser;
	console.log("getting friend("+ firstuser +")'s data");
	var qstr = mysql.format('select nickname2 from friend where nickname1 = ?;',[firstuser]);
	var query = connection.query(qstr, function(err,result){
		html = "";
		for( var i =0; i<result.length; i++){
			html+= JSON.stringify(result[i].nickname2)+"\n";
		}
		res.send(html);
	});

});
app.get('/delete/:firstuser/:seconduser',function(request,res,next){

	var firstuser = request.params.firstuser;
	var seconduser = request.params.seconduser;
	console.log("deleting friend from",firstuser,seconduser);

	var qstr = mysql.format('delete ignore from friend where nickname1= ? and nickname2 = ?',[firstuser,seconduser]);
	var query = connection.query(qstr, function(err,result){
		if (err) throw (err);
		res.send("1");
	});

});
app.get('/monday/:user',function(request,res,next){

	var user = request.params.user;
	var qstr = mysql.format('SELECT WEEKDAY(CURDATE()) as monday;');
	var query = connection.query(qstr,function(err,result){
		if(err) throw (err);
		console.log(result[0].monday);
		if(result[0].monday= 0){
			var qstr2 = mysql.format('select sum(success) as did from final where nickname = ? and time >= date(current_date()-7) and time <= date(current_date()-3);',user);
			var query2 = connection.query(qstr2,function(err2,result2){
				if(err2) throw err;
				console.log(result2[0].did);
				if(result2[0].did == 5){
					res.send("지난주는 풋피트 권장 운동량을 돌파하셨습니다! 지금과 같은 페이스를 유지한다면 심혈관질병률이 45%나 감소할 것입니다!");
				}
				else{
					res.send("지난주는 풋피트 운동 달성량을 돌파하지 못했습니다! 운동 달성량을 돌파한 경우 심혈관질환 발병률이 45%나 감소할 것입니다! 이번주에는 도전해보세요!");
				}
			});
		}
	});

});
app.get('/friend/:firstuser/:seconduser',function(request,res,next){
	var firstuser = request.params.firstuser;
	var seconduser = request.params.seconduser;
	console.log("in friend part");
	console.log(firstuser, seconduser);
	if ( firstuser == seconduser ){
		console.log("you cannot be friend with yourself");
		res.send("-1");

	}
	else{	

		var qstr = mysql.format(' select total from final where nickname = ?  union select -1 as total;',[seconduser]) ;
		var query = connection.query(qstr, function(err,result){

			console.log(result[0].total);
			if ( result[0].total < 0){ // if there's no such nickname
				console.log("there's no such nickname");
				res.send("-1");
			}
			else{
				console.log("there is your friend");
				var qstr2 = mysql.format('insert ignore into friend(nickname1,nickname2) values (?,?) ',[firstuser,seconduser]);
				var query2 = connection.query(qstr2,function(err2,result2){
					if(err2) throw err2;
					res.send("1");
				});		
			}

		});
	}
});
app.get('/', function(req, res) {
  res.end('Nice to meet you');
});


app.get('/month/:date/:username',function(request,res,next){
	var username = request.params.username;
	var date = request.params.date;
	console.log(username,date);
	var qstr = mysql.format('select total from final where nickname = ? and time = date((?)) union select -1 as total;',[username,date]);
	var query = connection.query(qstr, function(err,result){
		console.log(result[0]);
		html = ""
		html+=JSON.stringify(result[0].total);
		res.send(html);
	});
});

app.get('/add/:username',function(request,res,next){

	var username = request.params.username;
	var qstr = mysql.format('select count(nickname) as number from final where nickname = ?;',[username]);
	var query = connection.query(qstr, function(err,result){
		console.log(result[0]);
		console.log(result[0].number);
		if(result[0].number == 0){
		
			res.send("1");
			var qstr2 = mysql.format('insert ignore into final(nickname,total,time) values (?,0, DATE( CURRENT_DATE()));',[username]);
			var query2 = connection.query(qstr2, function(err,result_2){
				if (err) throw err;
				console.log(result_2[0]);
				console.log("database new nickname inserted okay = ?",result_2[0]);
				});


		}
		else{
			res.send("-1");
		}

	});

});

/*************************
 * for login
 * if there is no nickname, return -1
 * if there is nickname, go to that nickname's main page
 ***********************/
app.get('/users/:username',function(request,res,next){

	var username = request.params.username;
	//var time = request.params.time;
	console.log("param=" + request.query);

	//console.log(username,time);

	var qstr = mysql.format(' select total from final where nickname = ? and time = DATE( CURRENT_DATE() ) union select -1 as total;',[username]) ;
	var query = connection.query(qstr, function(err,result){
		
			console.log(result[0].total);
			if ( result[0].total < 0){
				console.log("there's no today data for that nickname");
				var qstr2 = mysql.format('select total from final where nickname = ? union select -1 as total;',[username]);
				var query2 = connection.query(qstr2,function(err2,result2){
					console.log(result2[0]);
					if(result2[0].total >= 0){
						console.log("that nickname exists");
						res.send("0");
					}
					else{
						console.log("there's no such nickname in database");
						res.send("-1");
					}
				});
			}

		else{
			console.log("there is today data and nickname!!");


			html = ""
			html += JSON.stringify(result[0].total);
			res.send(html);}

	});
});
/************************
 * getting exercise data
 * left_number and right_number increases 
 ************************/
app.get('/log', function(req, res){
	r = req.query;
	console.log("GET %j", r);

	var sql = mysql.format('select sum(left_num) as left_sum, sum(right_num) as right_sum from temp where nickname = ?  and today_date = date(current_date());',r.nickname);
        var query = connection.query(sql,function(err,result){
                if (err) throw err;
                if ((result[0].left_sum - result[0].right_sum >=30 )|| (result[0].right_sum - result[0].left_sum >=30 )){
                        console.log("Sensor is broken");
                        res.end("NOT OK: Sensor is broken");
			console.log("NOT OK: Sensor is broken");
                }
                else{
                        insert_temp(r.nickname,r.left_num,r.right_num);
                        res.end('OK:' + JSON.stringify(req.query));
                }
        });
});

//#5
app.get("/data", function(req, res) {
  console.log("param=" + req.query);
	var qstr = 'select * from final ';

  //var qstr = 'select * from sensors ';
  connection.query(qstr, function(err, rows, cols) {
    if (err) {
      throw err;
      res.send('query error: '+ qstr);
      return;
    }

    console.log("Got "+ rows.length +" records");
    html = ""
	  if ( rows.length > 300 ){
		  rows.length = 300;
	  }
    for (var i=0; i< rows.length; i++) {
      html += JSON.stringify(rows[i]);
    }
    res.send(html);
  });

});

/*
app.get('/graph', function (req, res) {
    console.log('got app.get(graph)');
    var html = fs.readFile('./graph.html', function (err, html) {
    html = " "+ html
    console.log('read file');
	    var qstr = 'select * from today ';
    connection.query(qstr, function(err, rows, cols) {
      if (err) throw err;
      var data = "";
      var comma = "";
      var temp = 1;
      var las =""
      for (var i=0; i< rows.length; i++) {
         r = rows[i];
         r.id+=32.5;
         data += comma + "[new Date(2019,02,31,07,"+ r.id*2 +",00),"+ r.temperature +"]";
         comma = ",";
         if(i== rows.length-1)
         {
                 las += r.time.getFullYear() + "-" + r.time.getMonth() + "-" +
                        r.time.getDate() + " " + r.time.getHours() +":" +
                        r.time.getMinutes() + ":" + r.time.getSeconds() ;
         }
      }
      var header = "data.addColumn('date', 'Time');"
      header += "data.addColumn('number', 'Temp');"
      html = html.replace("<%HEADER%>", header);
      html = html.replace("<%DATA%>", data);
      html = html.replace("<%LAST%>",las);
      res.writeHeader(200, {"Content-Type": "text/html"});
      res.write(html);
      res.end();
    });
  });
})*/


  var server = app.listen(3000,function () {
  var host = server.address().address
  var port = server.address().port
  console.log('listening at http://%s:%s', host, port)
});
