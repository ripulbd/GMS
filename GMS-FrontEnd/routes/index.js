
/*
 * GET home page.
 */

var mongo = require('mongodb');
var monk = require('monk');
var db = monk('localhost:27017/gmsTry');

exports.index = function(req, res){	
	var collection = db.get('gms');
	collection.find({},{},function(e,docs){
        res.render('index', {
            "userlist" : docs
        });
    });
  //res.render('index', { title: 'Glasgow Memory Server (GMS): Home Page!' });
};

exports.news = function(req, res){
  res.render('news', { title: 'Glasgow Memory Server (GMS): News' });
};

exports.blog = function(req, res){
  res.render('blog', { title: 'Glasgow Memory Server (GMS): Blog' });
};

exports.image = function(req, res){
  res.render('image', { title: 'Glasgow Memory Server (GMS): Image' });
};

exports.video = function(req, res){
  res.render('video', { title: 'Glasgow Memory Server (GMS): Video' });
};
exports.twitter = function(req, res){
  res.render('twitter', { title: 'Glasgow Memory Server (GMS): Twitter' });
};