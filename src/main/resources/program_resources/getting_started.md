# Getting Started
Welcome to the University of Groningen's Object Oriented Programming course! We're glad to have you. Read the instructions provided here carefully so that you can have a smooth start to the course.

## Setting Up
To begin, you'll need to download this repository you're looking at right now. To do that, run the following command (where `repository_url` is the URL of this repository):
```
git clone <repository_url>
```

Now you should have a local directory with the same name as this repository. Checkout your `development` branch, since you do not have permission as a student to make changes to the master branch. From here, we need to copy the files from the assignments repository into ours. To do this, we first add a new 'remote' to our git repository, and then pull from that repository's `master` branch.
```
git checkout development

git remote add assignments https://github.com/rug-oop/2019_assignments

git pull assignments master --allow-unrelated-histories
```

Now you should see all the files just as they appear in the assignments repository.

## Developing Your Code and Submitting
While previous knowledge of git may be useful, it should not really be needed for you to do well in this course.