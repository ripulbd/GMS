package main

import (
	//"bufio"
	"flag"
	"gopkg.in/mgo.v2"
	"gopkg.in/mgo.v2/bson"
	//"html/template"
	"text/template"
	"io/ioutil"
	"log"
	"net"
	"net/http"
	//"os"
	"regexp"
	//"strings"
	"fmt"
)

var (
	addr = flag.Bool("addr", false, "find open address and print to final-port.txt")
)

type Comment struct {
	UserName    string    `bson:"userName"`
	TimeStamp   string    `bson:"timeStamp"`
	CommentBody string    `bson:"commentBody"`
	UpVote      int       `bson:"upVote"`
	DownVote    int       `bson:"downVote"`
	Replies     []Comment `bson:"replies"`
}

type Image struct {
	Name string			`bson:"name"`
	Caption string 		`bson:"caption"`
}

type Page struct {
	Title       string    `bson:"title"`	
	Description string    `bson:"description"`
	TimeStamp   string    `bson:"timeStamp"`
	Category    string    `bson:"category"`
	Url         string    `bson:"url"`
	Source      string    `bson:"source"`
	MainStory   string    `bson:"mainStory"`
	Images      []Image	  `bson:"images"`
	Related     []string  `bson:"related"`
	Videos      []string  `bson:"videos"`
	Comments    []Comment `bson:"comments"`
	//Id			int
}

type TopPage struct {
	Headline string 
	Pages []Page	
}

var funcMap = template.FuncMap{
        // The name "inc" is what the function will be called in the template text.
        "inc": func(i int) int {
            return i + 1
        },
}

var templates = template.Must(template.New("test").Funcs(funcMap).ParseFiles("index.html", "news.html", "detailNews.html"))

//template.New("test").Funcs(funcMap).Parse(``)

func renderTemplate(w http.ResponseWriter, tmpl string, p *TopPage) {
	// Execute the template for each recipient.
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
	/*for _, r := range *p {
		err := templates.ExecuteTemplate(w, tmpl+".html", r)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	}*/
	
}

func renderDetailNews(w http.ResponseWriter, tmpl string, p *Page) {
	// Execute the template for each recipient.
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}	
}



func indexHandler(w http.ResponseWriter, r *http.Request) {
	//p := &Page{Title: title}
	
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{}).All(&result)
	
	if err != nil {
		log.Fatal(err)
	}
	
	topPage := TopPage{"Full contents of the GMS are shown below:", result}
	
	renderTemplate(w, "index", &topPage)
}

func etHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "Evening Times"}).All(&result)
	
	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from Evening Times are shown below:", result}
	
	renderTemplate(w, "news", &topPage)
}

func detailNewsHandler(w http.ResponseWriter, r *http.Request) {
	
	title := r.FormValue("query") 
	fmt.Printf("Query: %s\n", title)
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)
	
	c := session.DB("gmsTry").C("gmsNews")

	var result Page
	err = c.Find(bson.M{"title": title}).One(&result)
	
	if err != nil {
		log.Fatal(err)
	}
	
	
	renderDetailNews(w, "detailNews", &result)
}

func scotmanHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "http://www.scotsman.com"}).All(&result)
	
	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from Scotsman are shown below:", result}
	
	renderTemplate(w, "news", &topPage)
}

func drHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "http://www.dailyrecord.co.uk"}).All(&result)
	
	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from DailyRecord are shown below:", result}
	
	renderTemplate(w, "news", &topPage)
}

func bbcHandler(w http.ResponseWriter, r *http.Request) {
	session, err := mgo.Dial("localhost")
	if err != nil {
		panic(err)
	}
	defer session.Close()

	// Optional. Switch the session to a monotonic behavior.
	session.SetMode(mgo.Monotonic, true)

	c := session.DB("gmsTry").C("gmsNews")

	result := []Page{}
	err = c.Find(bson.M{"source": "http://www.bbc.co.uk"}).All(&result)
	
	if err != nil {
		log.Fatal(err)
	}
	topPage := TopPage{"News contents extracted from BBC are shown below:", result}
	
	renderTemplate(w, "news", &topPage)
}

//source: ''

var validPath = regexp.MustCompile("^/(index|save|view)$")

func makeHandler(fn func(http.ResponseWriter, *http.Request)) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		/*m := validPath.FindStringSubmatch(r.URL.Path)
		if m == nil {
			http.NotFound(w, r)
			return
		}*/
	

	//title := result.Title
	//fmt.Println("Phone:", result.mainStory)
	
		fn(w, r)
	}
}
func main() {
	flag.Parse()
	http.HandleFunc("/", makeHandler(indexHandler))
	http.HandleFunc("/et", makeHandler(etHandler))
	http.HandleFunc("/scotsman", makeHandler(scotmanHandler))
	http.HandleFunc("/bbc", makeHandler(bbcHandler))
	http.HandleFunc("/dr", makeHandler(drHandler))
	http.HandleFunc("/detailNews", makeHandler(detailNewsHandler))	
	http.Handle("/resources/", http.StripPrefix("/resources/", http.FileServer(http.Dir("resources"))))
	http.Handle("/resources/images/", http.StripPrefix("/resources/images/", http.FileServer(http.Dir("/home/ripul/resources/images/"))))

	if *addr {
		l, err := net.Listen("tcp", "127.0.0.1:0")
		if err != nil {
			log.Fatal(err)
		}
		err = ioutil.WriteFile("final-port.txt", []byte(l.Addr().String()), 0644)
		if err != nil {
			log.Fatal(err)
		}
		s := &http.Server{}
		s.Serve(l)
		return
	}

	http.ListenAndServe(":8080", nil)
}
