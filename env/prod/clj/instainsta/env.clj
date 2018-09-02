(ns instainsta.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[instainsta started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[instainsta has shut down successfully]=-"))
   :middleware identity})
