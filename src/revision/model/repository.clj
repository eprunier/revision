(ns revision.model.repository)

(defprotocol Repository
  (log [this] "Return repository history")
  (head [this] "Return the HEAD revision")
  (revision [this rev] "Return the sprecified revision"))
