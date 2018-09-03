(ns instainsta.routes.home
  (:require [instainsta.layout :as layout]
            [instainsta.db.core :as db]
            [instainsta.config :refer [env]]
            [amazonica.aws.s3 :as s3]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :as response]
            [clojure.java.io :as io]))

(defn s3-options [] (merge {:endpoint "us-east-1"}
                           (select-keys env [:access-key :secret-key])))

(defn by-user
  [user]
  (fn [x] (clojure.string/starts-with? x (str user "__"))))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn success-page []
  (layout/render "success.html"))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [user]
       (let [objects (s3/list-objects-v2 (s3-options) {:bucket-name "futurice-challenge"})
             photos (->> (:object-summaries objects)
                         (map :key)
                         (filter (if user (by-user user) identity))
                         (map (partial str "https://s3.amazonaws.com/futurice-challenge/")))]
         (layout/render "index.html"
                        {:photos photos})))
  (POST "/" [file nick]
        ;; file with same name will be overwrited, so in production mode, gen a
        ;; random string as filename
        (s3/put-object (s3-options)
                       :bucket-name "futurice-challenge"
                       :key (str nick "__" (:filename file))
                       :file (:tempfile file))
        (response/redirect "/success"))
  (GET "/success" [] (success-page))
  (GET "/about" [] (about-page)))
