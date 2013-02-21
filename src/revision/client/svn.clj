(ns revision.client.svn
  (:import [org.tmatesoft.svn.core SVNURL]
           [org.tmatesoft.svn.core.io SVNRepositoryFactory SVNRepository]
           [org.tmatesoft.svn.core.wc SVNClientManager SVNWCUtil]
           [org.tmatesoft.svn.core.internal.io.fs FSRepositoryFactory]
           [org.tmatesoft.svn.core.internal.io.dav DAVRepositoryFactory DAVRepository]
           [org.tmatesoft.svn.core.internal.io.svn SVNRepositoryFactoryImpl])
  (:require [revision.repository]))

(DAVRepositoryFactory/setup)
(SVNRepositoryFactoryImpl/setup)
(FSRepositoryFactory/setup)

(defn get-repo
  "Load SVN repository"
  ([uri]
     (get-repo uri "" ""))
  ([uri login password]
     (let [url (SVNURL/parseURIEncoded uri)
           repo (SVNRepositoryFactory/create url)]
       (when (or login password)
         (let [authManager (SVNWCUtil/createDefaultAuthenticationManager login password)]    
           (.setAuthenticationManager repo authManager)))
       repo)))

(defn- get-head
  "Get head revision"
  [^SVNRepository repo]
  (.getLatestRevision repo))

(defn- get-history
  "Get repository history between start and end revisions"
  ([^SVNRepository repo start end]
     (let [target-paths (into-array [""])
           entries nil
           changed-path true
           strict-node true]
       (-> repo
           (.log target-paths entries start end changed-path strict-node))))
  ([^SVNRepository repo limit]
     (let [start (get-head repo)
           end (- start (- limit 1))]
       (get-history repo start end ))))

;;; Repository protocol implementation
(extend-protocol revision.repository/Repository
  org.tmatesoft.svn.core.io.SVNRepository
  (log
    ([this start end]
       (get-history this start end))
    ([this limit]
       (get-history this limit)))
  (show
    ([this]
       (let [head (get-head this)]
         (get-history this head head)))
    ([this rev]
       (get-history this rev rev)))
  (head [this]
    (get-head this)))
