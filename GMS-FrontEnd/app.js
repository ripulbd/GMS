
/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , http = require('http')
  , path = require('path');

//Database
var mongo = require('mongoskin');
var db = mongo.db("mongodb://localhost:27017/gms", {native_parser:true});

var app = express();

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

//Make our db accessible to our router
app.use(function(req,res,next){
    req.db = db;
    next();
});

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
app.get('/news', routes.news);
app.get('/bbc', routes.bbc);
app.get('/scotsman', routes.scotsman);
app.get('/et', routes.et);
app.get('/blogger', routes.blogger);
app.get('/wordpress', routes.wordpress);
app.get('/bbcvid', routes.bbcvid);
app.get('/youtube', routes.youtube);
app.get('/flickr', routes.flickr);
app.get('/imgur', routes.imgur);
app.get('/insta', routes.insta);
app.get('/blog', routes.blog);
app.get('/image', routes.image);
app.get('/video', routes.video);
app.get('/twitter', routes.twitter);
app.post('/detailNews', routes.detailNews);
app.get('/users', user.list);


http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
