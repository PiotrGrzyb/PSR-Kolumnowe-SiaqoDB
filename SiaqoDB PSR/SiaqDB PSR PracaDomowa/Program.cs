using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Sqo;

namespace SiaqDB_PSR_PracaDomowa
{
    class Program
    {
        static void Main(string[] args)
        {
            int menuChoice = 0;

            Siaqodb siaqodb = new Siaqodb("Animals");

            do
            {
                Console.WriteLine("[1] - View animals");
                Console.WriteLine("[2] - Add new animal");
                Console.WriteLine("[3] - View animal by ID");
                Console.WriteLine("[4] - View animal by species");
                Console.WriteLine("[5] - Update animal audience rating");
                Console.WriteLine("[6] - Delete animal by ID");
                Console.WriteLine("[7] - Calculate average audience rating for all animals");
                Console.WriteLine("[8] - Exit");

                menuChoice = Convert.ToInt32(Console.ReadLine());

                if (menuChoice == 1)
                {
                    IObjectList<SiaqoDB_PSR.Animal> animals = siaqodb.LoadAll<SiaqoDB_PSR.Animal>();
                    foreach(SiaqoDB_PSR.Animal e in animals)
                    {
                        Console.WriteLine($"ID: {e.OID}, name: {e.name}, species: {e.species}, age: {e.age}, awards: {e.awards}, audience rating: {e.audianceRating}");
                    }
                }
                if (menuChoice == 2)
                {
                    Console.WriteLine("New animal ID: ");
                    int id = Convert.ToInt32(Console.ReadLine());
                    Console.WriteLine("Animal name: ");
                    string name = Console.ReadLine();
                    Console.WriteLine("Animal species: ");
                    string species = Console.ReadLine();
                    Console.WriteLine("Animal age: ");
                    int age = Convert.ToInt32(Console.ReadLine());
                    Console.WriteLine("Animal award: ");
                    string award = Console.ReadLine();
                    Console.WriteLine("Animal audeince rating: ");
                    int audienceRating = Convert.ToInt32(Console.ReadLine());

                    SiaqoDB_PSR.Animal animal = new SiaqoDB_PSR.Animal();
                    animal.OID = id;
                    animal.name = name;
                    animal.species = species;
                    animal.age = age;
                    animal.awards = award;
                    animal.audianceRating = audienceRating;
                    siaqodb.StoreObject(animal);
                }
                if(menuChoice == 3)
                {
                    Console.WriteLine("Animal ID: ");
                    int id = Convert.ToInt32(Console.ReadLine());
                    var query = from SiaqoDB_PSR.Animal e in siaqodb
                                where e.OID.Equals(id)
                                select e;
                    foreach (SiaqoDB_PSR.Animal e in query)
                    {
                        Console.WriteLine($"ID: {e.OID}, name: {e.name}, species: {e.species}, age: {e.age}, awards: {e.awards}, audience rating: {e.audianceRating}");
                    }

                }
                if(menuChoice == 4)
                {
                    Console.WriteLine("Animal species: ");
                    string species = Console.ReadLine();
                    var query = from SiaqoDB_PSR.Animal e in siaqodb
                                where e.species.Contains(species)
                                select e;
                    foreach (SiaqoDB_PSR.Animal e in query)
                    {
                        Console.WriteLine($"ID: {e.OID}, name: {e.name}, species: {e.species}, age: {e.age}, awards: {e.awards}, audience rating: {e.audianceRating}");
                    }
                }
                if(menuChoice == 5)
                {
                    Console.WriteLine("ID of an Animal to be modified: ");
                    int id = Convert.ToInt32(Console.ReadLine());

                    var animalByID = siaqodb.LoadObjectByOID<SiaqoDB_PSR.Animal>(id);
                    if(animalByID == null)
                        Console.WriteLine("There is no such animal with ID: " + id);
                    else 
                    {
                        Console.WriteLine($"ID: {animalByID.OID}, name: {animalByID.name}, species: {animalByID.species}, age: {animalByID.age}, awards: {animalByID.awards}, audience rating: {animalByID.audianceRating}");
                        Console.WriteLine("Modify audience rating: ");
                        int audienceRating = Convert.ToInt32(Console.ReadLine());
                        animalByID.audianceRating = audienceRating;
                        siaqodb.StoreObject(animalByID);
                    }


                }
                if(menuChoice == 6)
                {
                    Console.WriteLine("ID of an animal to be deleted: ");
                    int id = Convert.ToInt32(Console.ReadLine());
                    var animalToBeDeleted = siaqodb.LoadObjectByOID<SiaqoDB_PSR.Animal>(id);
                    if(animalToBeDeleted == null)         
                        Console.WriteLine("There is no such animal with ID: " + id);               
                    else
                        siaqodb.Delete(animalToBeDeleted);
                }
                if(menuChoice == 7)
                {
                    IObjectList<SiaqoDB_PSR.Animal> animals = siaqodb.LoadAll<SiaqoDB_PSR.Animal>();
                    double sum = 0;
                    int i = 0;
                    foreach (SiaqoDB_PSR.Animal e in animals)
                    {
                        sum += e.audianceRating;
                        i++;
                    }
                    Console.WriteLine("Average audience rating of all animals is: " + sum/i);


                }

            } while (menuChoice != 8);
        }
    }
}
