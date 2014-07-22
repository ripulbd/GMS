
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.render('index', { title: 'Glasgow Memory Server (GMS): Home Page!!' });
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