(ns revision.repository)

(defprotocol Repository
  (log [this n] [this start end]
    "Return repository history
     * if N is provided: from HEAD with N revisions
     * if START and END are provided: from START revision to END revision")
  (show [this] [this rev]
    "Returns the REV contents if provided, otherwise the HEAD contents")
  (head [this] "Return the HEAD revision"))
