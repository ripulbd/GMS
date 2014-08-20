
/*
 * GET home page.
 */

var mongo = require('mongodb');
var monk = require('monk');
var db = monk('localhost:27017/gmsTry');
var collectionName = "gmsNews";

exports.index = function(req, res){	
	var collection = db.get(collectionName);
	collection.find({},{},function(e,docs){
		var fileName = docs['fileName'];
        res.render('index', {
            "userlist" : docs,
            "fileName" : fileName
        });
    });
  //res.render('index', { title: 'Glasgow Memory Server (GMS): Home Page!' });
};

exports.news = function(req, res){
	
	var collection = db.get(collectionName);
	collection.find({category: 'news'},{},function(e,docs){
		var fileName = docs['fileName'];
        res.render('news', {
            "userlist" : docs,
            "fileName" : fileName,
            "title" : "Glasgow Memory Server (GMS): News",
            "minihead" : "News contents extracted from news sites are shown below:"
        });
    });
  //res.render('news', { title: 'Glasgow Memory Server (GMS): News' });
};

exports.detailNews = function(req, res){
	
	var collection = db.get(collectionName);
	var query = req.body.query;
	console.log("Query:" + query);
	collection.find({title: query},{},function(e,docs){
		var fileName = docs['fileName'];
		console.log("Main Story:" + docs[0].mainStory);
        res.render('detailNews', {
            "userlist" : docs,
            "fileName" : fileName,
            "query"	: query,
            "title" : "Glasgow Memory Server (GMS): Detail news",
            "minihead" : "Detail news follows:"
        });
    });
  //res.render('news', { title: 'Glasgow Memory Server (GMS): News' });
};

exports.bbc = function(req, res){
	//find({gender: 'f'});
	var collection = db.get(collectionName);
	collection.find({source: 'BBC'},{},function(e,docs){
		var fileName = docs['fileName'];
        res.render('news', {
            "userlist" : docs,
            "fileName" : fileName,
            "title" : "Glasgow Memory Server (GMS): News",
            "minihead" : "News contents extracted from BBC are shown below:"
        });
    });
  //res.render('news', { title: 'Glasgow Memory Server (GMS): News' });
};

exports.scotsman = function(req, res){
	//find({gender: 'f'});
	var collection = db.get(collectionName);
	collection.find({source: 'http://www.scotsman.com'},{},function(e,docs){
		var fileName = docs['fileName'];
        res.render('news', {
            "userlist" : docs,
            "fileName" : fileName,
            "title" : "Glasgow Memory Server (GMS): News",
            "minihead" : "News contents extracted from the Scotsman are shown below:"
        });
    });
  //res.render('news', { title: 'Glasgow Memory Server (GMS): News' });
};

exports.et = function(req, res){
	//find({gender: 'f'});
	var collection = db.get(collectionName);
	collection.find({source: 'Evening Times'},{},function(e,docs){
		var fileName = docs['fileName'];
        res.render('news', {
            "userlist" : docs,
            "fileName" : fileName,
            "title" : "Glasgow Memory Server (GMS): News",
            "minihead" : "News contents extracted from Evening Times are shown below:"
        });
    });
  //res.render('news', { title: 'Glasgow Memory Server (GMS): News' });
};

exports.blog = function(req, res){
  res.render('blog', { title: 'Glasgow Memory Server (GMS): Blog' });
};

exports.blogger = function(req, res){
  res.render('blog', { title: 'Glasgow Memory Server (GMS): Blog' });
};

exports.wordpress = function(req, res){
  res.render('blog', { title: 'Glasgow Memory Server (GMS): Blog' });
};

exports.image = function(req, res){
	var collection = db.get(collectionName);
	collection.find({category: 'image'},{},function(e,docs){
		var fileName = docs['fileName'];
        res.render('image', {
            "userlist" : docs,
            "fileName" : fileName,
            "title" : "Glasgow Memory Server (GMS): Image",
            "minihead" : "Images extracted from different sites are shown below:"
        });
    });
  //res.render('image', { title: 'Glasgow Memory Server (GMS): Image' });
};

exports.flickr = function(req, res){
	var collection = db.get(collectionName);
	collection.find({source: 'Flickr'},{},function(e,docs){
		var fileName = docs['fileName'];
        res.render('image', {
            "userlist" : docs,
            "fileName" : fileName,
            "title" : "Glasgow Memory Server (GMS): Image",
            "minihead" : "Images extracted from flickr are shown below:"
        });
    });
  //res.render('image', { title: 'Glasgow Memory Server (GMS): Image' });
};

exports.imgur = function(req, res){
	var collection = db.get(collectionName);
	collection.find({source: 'imgur.com'},{},function(e,docs){
		var fileName = docs['fileName'];
        res.render('image', {
            "userlist" : docs,
            "fileName" : fileName,
            "title" : "Glasgow Memory Server (GMS): Image",
            "minihead" : "Images extracted from imgur are shown below:"
        });
    });
  //res.render('image', { title: 'Glasgow Memory Server (GMS): Image' });
};

exports.insta = function(req, res){
  res.render('image', { title: 'Glasgow Memory Server (GMS): Image' });
};
	
exports.video = function(req, res){
  res.render('video', { title: 'Glasgow Memory Server (GMS): Video' });
};

exports.youtube = function(req, res){
  res.render('video', { title: 'Glasgow Memory Server (GMS): Video' });
};

exports.bbcvid = function(req, res){
  res.render('video', { title: 'Glasgow Memory Server (GMS): Video' });
};
exports.twitter = function(req, res){
  res.render('twitter', { title: 'Glasgow Memory Server (GMS): Twitter' });
};